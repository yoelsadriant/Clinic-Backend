package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.HistoryInstallmentRequest;
import com.ultimindstudio.clinic.dto.response.HistoryInstallmentResponse;
import com.ultimindstudio.clinic.entity.Client;
import com.ultimindstudio.clinic.entity.HistoryInstallment;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.ClientRepository;
import com.ultimindstudio.clinic.repository.HistoryInstallmentRepository;
import com.ultimindstudio.clinic.service.HistoryInstallmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HistoryInstallmentServiceImpl implements HistoryInstallmentService {

    private final HistoryInstallmentRepository historyInstallmentRepository;
    private final ClientRepository clientRepository;

    @Override @Transactional(readOnly = true)
    public Page<HistoryInstallmentResponse> findAll(Pageable pageable) {
        return historyInstallmentRepository.findAll(pageable).map(this::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public HistoryInstallmentResponse findById(Long id) { return toResponse(getById(id)); }

    @Override
    public HistoryInstallmentResponse create(HistoryInstallmentRequest req) {
        Client client = findClient(req.clientId());
        HistoryInstallment hi = HistoryInstallment.builder().client(client)
                .date(req.date()).payment(req.payment()).build();
        return toResponse(historyInstallmentRepository.save(hi));
    }

    @Override
    public HistoryInstallmentResponse update(Long id, HistoryInstallmentRequest req) {
        HistoryInstallment hi = getById(id);
        hi.setClient(findClient(req.clientId())); hi.setDate(req.date()); hi.setPayment(req.payment());
        return toResponse(historyInstallmentRepository.save(hi));
    }

    @Override
    public void delete(Long id) { getById(id); historyInstallmentRepository.deleteById(id); }

    private HistoryInstallment getById(Long id) {
        return historyInstallmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("HistoryInstallment", "id", id));
    }
    private Client findClient(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
    }

    private HistoryInstallmentResponse toResponse(HistoryInstallment hi) {
        return new HistoryInstallmentResponse(hi.getId(), hi.getClient().getId(),
                hi.getClient().getName(), hi.getDate(), hi.getPayment());
    }
}

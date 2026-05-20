package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.InstallmentRequest;
import com.ultimindstudio.clinic.dto.response.InstallmentResponse;
import com.ultimindstudio.clinic.entity.Client;
import com.ultimindstudio.clinic.entity.Installment;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.ClientRepository;
import com.ultimindstudio.clinic.repository.InstallmentRepository;
import com.ultimindstudio.clinic.service.InstallmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InstallmentServiceImpl implements InstallmentService {

    private final InstallmentRepository installmentRepository;
    private final ClientRepository clientRepository;

    @Override @Transactional(readOnly = true)
    public Page<InstallmentResponse> findAll(Pageable pageable) {
        return installmentRepository.findAll(pageable).map(this::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public InstallmentResponse findById(Long id) { return toResponse(getById(id)); }

    @Override
    public InstallmentResponse create(InstallmentRequest req) {
        Client client = findClient(req.clientId());
        Installment i = Installment.builder().client(client).total(req.total())
                .date(req.date()).dueDate(req.dueDate()).remaining(req.remaining()).build();
        return toResponse(installmentRepository.save(i));
    }

    @Override
    public InstallmentResponse update(Long id, InstallmentRequest req) {
        Installment i = getById(id);
        i.setClient(findClient(req.clientId())); i.setTotal(req.total());
        i.setDate(req.date()); i.setDueDate(req.dueDate()); i.setRemaining(req.remaining());
        return toResponse(installmentRepository.save(i));
    }

    @Override
    public void delete(Long id) { getById(id); installmentRepository.deleteById(id); }

    private Installment getById(Long id) {
        return installmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Installment", "id", id));
    }
    private Client findClient(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
    }

    private InstallmentResponse toResponse(Installment i) {
        return new InstallmentResponse(i.getId(), i.getClient().getId(), i.getClient().getName(),
                i.getTotal(), i.getDate(), i.getDueDate(), i.getRemaining());
    }
}

package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.HistoryRequest;
import com.ultimindstudio.clinic.dto.response.HistoryResponse;
import com.ultimindstudio.clinic.entity.*;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.*;
import com.ultimindstudio.clinic.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final StaffRepository staffRepository;
    private final ClientPackageRepository clientPackageRepository;
    private final TreatmentRepository treatmentRepository;

    @Override @Transactional(readOnly = true)
    public Page<HistoryResponse> findAll(Pageable pageable) {
        return historyRepository.findAll(pageable).map(this::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public HistoryResponse findById(Long id) { return toResponse(getById(id)); }

    @Override
    public HistoryResponse create(HistoryRequest req) {
        History h = History.builder().date(req.date())
                .staff(findStaff(req.staffId()))
                .clientPackage(findClientPackage(req.clientPackageId()))
                .treatment(findTreatment(req.treatmentId()))
                .note(req.note()).build();
        return toResponse(historyRepository.save(h));
    }

    @Override
    public HistoryResponse update(Long id, HistoryRequest req) {
        History h = getById(id);
        h.setDate(req.date()); h.setStaff(findStaff(req.staffId()));
        h.setClientPackage(findClientPackage(req.clientPackageId()));
        h.setTreatment(findTreatment(req.treatmentId())); h.setNote(req.note());
        return toResponse(historyRepository.save(h));
    }

    @Override
    public void delete(Long id) { getById(id); historyRepository.deleteById(id); }

    private History getById(Long id) {
        return historyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("History", "id", id));
    }
    private Staff findStaff(Long id) {
        return staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));
    }
    private ClientPackage findClientPackage(Long id) {
        return clientPackageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ClientPackage", "id", id));
    }
    private Treatment findTreatment(Long id) {
        return treatmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Treatment", "id", id));
    }

    private HistoryResponse toResponse(History h) {
        return new HistoryResponse(h.getId(), h.getDate(),
                h.getStaff().getId(), h.getStaff().getName(),
                h.getClientPackage().getId(),
                h.getClientPackage().getClient().getId(),
                h.getClientPackage().getClient().getName(),
                h.getTreatment().getId(), h.getTreatment().getName(),
                h.getNote());
    }
}

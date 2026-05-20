package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.TreatmentRequest;
import com.ultimindstudio.clinic.dto.response.TreatmentResponse;
import com.ultimindstudio.clinic.entity.Treatment;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.TreatmentRepository;
import com.ultimindstudio.clinic.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;

    @Override @Transactional(readOnly = true)
    public Page<TreatmentResponse> findAll(Pageable pageable) {
        return treatmentRepository.findAll(pageable).map(t -> new TreatmentResponse(t.getId(), t.getName(), t.getPrice()));
    }

    @Override @Transactional(readOnly = true)
    public TreatmentResponse findById(Long id) {
        Treatment t = getById(id); return new TreatmentResponse(t.getId(), t.getName(), t.getPrice());
    }

    @Override
    public TreatmentResponse create(TreatmentRequest req) {
        Treatment t = Treatment.builder().name(req.name()).price(req.price()).build();
        Treatment saved = treatmentRepository.save(t);
        return new TreatmentResponse(saved.getId(), saved.getName(), saved.getPrice());
    }

    @Override
    public TreatmentResponse update(Long id, TreatmentRequest req) {
        Treatment t = getById(id); t.setName(req.name()); t.setPrice(req.price());
        Treatment saved = treatmentRepository.save(t);
        return new TreatmentResponse(saved.getId(), saved.getName(), saved.getPrice());
    }

    @Override
    public void delete(Long id) { getById(id); treatmentRepository.deleteById(id); }

    private Treatment getById(Long id) {
        return treatmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Treatment", "id", id));
    }
}

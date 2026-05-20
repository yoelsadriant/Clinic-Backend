package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.LookupRequest;
import com.ultimindstudio.clinic.dto.LookupResponse;
import com.ultimindstudio.clinic.entity.TypePayment;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.TypePaymentRepository;
import com.ultimindstudio.clinic.service.LookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("typePaymentService")
@RequiredArgsConstructor
@Transactional
public class TypePaymentServiceImpl implements LookupService {

    private final TypePaymentRepository repository;

    @Override @Transactional(readOnly = true)
    public Page<LookupResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(t -> new LookupResponse(t.getId(), t.getName()));
    }

    @Override @Transactional(readOnly = true)
    public LookupResponse findById(Long id) {
        TypePayment t = getById(id); return new LookupResponse(t.getId(), t.getName());
    }

    @Override
    public LookupResponse create(LookupRequest req) {
        TypePayment saved = repository.save(TypePayment.builder().name(req.name()).build());
        return new LookupResponse(saved.getId(), saved.getName());
    }

    @Override
    public LookupResponse update(Long id, LookupRequest req) {
        TypePayment t = getById(id); t.setName(req.name());
        TypePayment saved = repository.save(t);
        return new LookupResponse(saved.getId(), saved.getName());
    }

    @Override
    public void delete(Long id) { getById(id); repository.deleteById(id); }

    private TypePayment getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("TypePayment", "id", id));
    }
}

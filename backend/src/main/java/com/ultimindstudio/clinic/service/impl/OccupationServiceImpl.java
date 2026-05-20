package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.LookupRequest;
import com.ultimindstudio.clinic.dto.LookupResponse;
import com.ultimindstudio.clinic.entity.Occupation;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.OccupationRepository;
import com.ultimindstudio.clinic.service.LookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("occupationService")
@RequiredArgsConstructor
@Transactional
public class OccupationServiceImpl implements LookupService {

    private final OccupationRepository repository;

    @Override @Transactional(readOnly = true)
    public Page<LookupResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(o -> new LookupResponse(o.getId(), o.getName()));
    }

    @Override @Transactional(readOnly = true)
    public LookupResponse findById(Long id) { return new LookupResponse(getById(id).getId(), getById(id).getName()); }

    @Override
    public LookupResponse create(LookupRequest req) {
        Occupation saved = repository.save(Occupation.builder().name(req.name()).build());
        return new LookupResponse(saved.getId(), saved.getName());
    }

    @Override
    public LookupResponse update(Long id, LookupRequest req) {
        Occupation o = getById(id); o.setName(req.name());
        Occupation saved = repository.save(o);
        return new LookupResponse(saved.getId(), saved.getName());
    }

    @Override
    public void delete(Long id) { getById(id); repository.deleteById(id); }

    private Occupation getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Occupation", "id", id));
    }
}

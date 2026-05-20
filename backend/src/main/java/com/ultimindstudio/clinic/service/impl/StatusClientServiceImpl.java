package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.LookupRequest;
import com.ultimindstudio.clinic.dto.LookupResponse;
import com.ultimindstudio.clinic.entity.StatusClient;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.StatusClientRepository;
import com.ultimindstudio.clinic.service.LookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("statusClientService")
@RequiredArgsConstructor
@Transactional
public class StatusClientServiceImpl implements LookupService {

    private final StatusClientRepository repository;

    @Override @Transactional(readOnly = true)
    public Page<LookupResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(s -> new LookupResponse(s.getId(), s.getName()));
    }

    @Override @Transactional(readOnly = true)
    public LookupResponse findById(Long id) {
        StatusClient s = getById(id); return new LookupResponse(s.getId(), s.getName());
    }

    @Override
    public LookupResponse create(LookupRequest req) {
        StatusClient saved = repository.save(StatusClient.builder().name(req.name()).build());
        return new LookupResponse(saved.getId(), saved.getName());
    }

    @Override
    public LookupResponse update(Long id, LookupRequest req) {
        StatusClient s = getById(id); s.setName(req.name());
        StatusClient saved = repository.save(s);
        return new LookupResponse(saved.getId(), saved.getName());
    }

    @Override
    public void delete(Long id) { getById(id); repository.deleteById(id); }

    private StatusClient getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("StatusClient", "id", id));
    }
}

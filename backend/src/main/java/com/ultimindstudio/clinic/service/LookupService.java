package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.LookupRequest;
import com.ultimindstudio.clinic.dto.LookupResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LookupService {
    Page<LookupResponse> findAll(Pageable pageable);
    LookupResponse findById(Long id);
    LookupResponse create(LookupRequest request);
    LookupResponse update(Long id, LookupRequest request);
    void delete(Long id);
}

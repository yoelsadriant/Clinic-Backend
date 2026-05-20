package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.SalesRequest;
import com.ultimindstudio.clinic.dto.response.SalesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalesService {
    Page<SalesResponse> findAll(Pageable pageable);
    SalesResponse findById(Long id);
    SalesResponse create(SalesRequest request);
    SalesResponse update(Long id, SalesRequest request);
    void delete(Long id);
}

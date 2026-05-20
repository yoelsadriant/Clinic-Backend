package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.InstallmentRequest;
import com.ultimindstudio.clinic.dto.response.InstallmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InstallmentService {
    Page<InstallmentResponse> findAll(Pageable pageable);
    InstallmentResponse findById(Long id);
    InstallmentResponse create(InstallmentRequest request);
    InstallmentResponse update(Long id, InstallmentRequest request);
    void delete(Long id);
}

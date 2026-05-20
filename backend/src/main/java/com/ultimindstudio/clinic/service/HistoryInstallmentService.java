package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.HistoryInstallmentRequest;
import com.ultimindstudio.clinic.dto.response.HistoryInstallmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryInstallmentService {
    Page<HistoryInstallmentResponse> findAll(Pageable pageable);
    HistoryInstallmentResponse findById(Long id);
    HistoryInstallmentResponse create(HistoryInstallmentRequest request);
    HistoryInstallmentResponse update(Long id, HistoryInstallmentRequest request);
    void delete(Long id);
}

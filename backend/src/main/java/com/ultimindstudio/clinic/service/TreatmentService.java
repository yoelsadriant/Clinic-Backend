package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.TreatmentRequest;
import com.ultimindstudio.clinic.dto.response.TreatmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TreatmentService {
    Page<TreatmentResponse> findAll(Pageable pageable);
    TreatmentResponse findById(Long id);
    TreatmentResponse create(TreatmentRequest request);
    TreatmentResponse update(Long id, TreatmentRequest request);
    void delete(Long id);
}

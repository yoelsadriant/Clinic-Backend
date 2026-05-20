package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.StaffRequest;
import com.ultimindstudio.clinic.dto.response.StaffResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StaffService {
    Page<StaffResponse> findAll(Pageable pageable);
    StaffResponse findById(Long id);
    StaffResponse create(StaffRequest request);
    StaffResponse update(Long id, StaffRequest request);
    void delete(Long id);
}

package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.ScheduleRequest;
import com.ultimindstudio.clinic.dto.response.ScheduleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {
    Page<ScheduleResponse> findAll(Pageable pageable);
    ScheduleResponse findById(Long id);
    ScheduleResponse create(ScheduleRequest request);
    ScheduleResponse update(Long id, ScheduleRequest request);
    void delete(Long id);
}

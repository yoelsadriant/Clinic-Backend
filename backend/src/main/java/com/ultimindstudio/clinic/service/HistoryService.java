package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.HistoryRequest;
import com.ultimindstudio.clinic.dto.response.HistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryService {
    Page<HistoryResponse> findAll(Pageable pageable);
    HistoryResponse findById(Long id);
    HistoryResponse create(HistoryRequest request);
    HistoryResponse update(Long id, HistoryRequest request);
    void delete(Long id);
}

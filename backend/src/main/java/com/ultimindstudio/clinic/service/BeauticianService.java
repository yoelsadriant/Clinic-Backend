package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.BeauticianRequest;
import com.ultimindstudio.clinic.dto.response.BeauticianResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeauticianService {
    Page<BeauticianResponse> findAll(Pageable pageable);
    BeauticianResponse findById(Long id);
    BeauticianResponse create(BeauticianRequest request);
    BeauticianResponse update(Long id, BeauticianRequest request);
    void delete(Long id);
}

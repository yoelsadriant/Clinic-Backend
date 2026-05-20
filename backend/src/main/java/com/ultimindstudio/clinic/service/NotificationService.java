package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.NotificationRequest;
import com.ultimindstudio.clinic.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Page<NotificationResponse> findAll(Pageable pageable);
    NotificationResponse findById(Long id);
    NotificationResponse create(NotificationRequest request);
    NotificationResponse update(Long id, NotificationRequest request);
    void delete(Long id);
}

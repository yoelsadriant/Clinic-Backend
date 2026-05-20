package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.NotificationRequest;
import com.ultimindstudio.clinic.dto.response.NotificationResponse;
import com.ultimindstudio.clinic.entity.Notification;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.NotificationRepository;
import com.ultimindstudio.clinic.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override @Transactional(readOnly = true)
    public Page<NotificationResponse> findAll(Pageable pageable) {
        return notificationRepository.findAll(pageable).map(n -> new NotificationResponse(n.getId(), n.getDateTime(), n.getDescription()));
    }

    @Override @Transactional(readOnly = true)
    public NotificationResponse findById(Long id) {
        Notification n = getById(id); return new NotificationResponse(n.getId(), n.getDateTime(), n.getDescription());
    }

    @Override
    public NotificationResponse create(NotificationRequest req) {
        Notification n = Notification.builder().dateTime(req.dateTime()).description(req.description()).build();
        Notification saved = notificationRepository.save(n);
        return new NotificationResponse(saved.getId(), saved.getDateTime(), saved.getDescription());
    }

    @Override
    public NotificationResponse update(Long id, NotificationRequest req) {
        Notification n = getById(id); n.setDateTime(req.dateTime()); n.setDescription(req.description());
        Notification saved = notificationRepository.save(n);
        return new NotificationResponse(saved.getId(), saved.getDateTime(), saved.getDescription());
    }

    @Override
    public void delete(Long id) { getById(id); notificationRepository.deleteById(id); }

    private Notification getById(Long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notification", "id", id));
    }
}

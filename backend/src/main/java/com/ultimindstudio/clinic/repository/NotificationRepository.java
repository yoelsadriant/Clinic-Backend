package com.ultimindstudio.clinic.repository;

import com.ultimindstudio.clinic.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByDateTimeBetween(LocalDateTime from, LocalDateTime to);
    List<Notification> findByDateTimeAfter(LocalDateTime dateTime);
}

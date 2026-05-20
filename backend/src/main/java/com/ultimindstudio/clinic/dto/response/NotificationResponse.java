package com.ultimindstudio.clinic.dto.response;

import java.time.LocalDateTime;

public record NotificationResponse(Long id, LocalDateTime dateTime, String description) {}

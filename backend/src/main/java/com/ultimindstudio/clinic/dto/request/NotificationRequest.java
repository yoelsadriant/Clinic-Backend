package com.ultimindstudio.clinic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record NotificationRequest(
        @NotNull LocalDateTime dateTime,
        @NotBlank String description
) {}

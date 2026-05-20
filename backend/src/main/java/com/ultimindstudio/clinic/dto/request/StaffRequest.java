package com.ultimindstudio.clinic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StaffRequest(
        @NotBlank String name,
        @NotBlank String address,
        @NotBlank String phone,
        @NotBlank String workplace,
        boolean active,
        @NotNull Long occupationId
) {}

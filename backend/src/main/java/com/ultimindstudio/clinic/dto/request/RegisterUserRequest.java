package com.ultimindstudio.clinic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterUserRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotNull Long typeAppId
) {}

package com.ultimindstudio.clinic.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ClientRequest(
        @NotBlank String name,
        @NotBlank String sex,
        @NotBlank String address,
        @NotBlank String phone,
        @Email @NotBlank String email,
        @NotNull LocalDate registeredDate,
        @NotNull Long staffId
) {}

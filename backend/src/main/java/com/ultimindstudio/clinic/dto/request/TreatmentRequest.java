package com.ultimindstudio.clinic.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TreatmentRequest(
        @NotBlank String name,
        @NotNull @DecimalMin("0.00") BigDecimal price
) {}

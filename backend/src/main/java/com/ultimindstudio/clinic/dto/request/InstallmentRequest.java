package com.ultimindstudio.clinic.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentRequest(
        @NotNull Long clientId,
        @NotNull @DecimalMin("0.00") BigDecimal total,
        @NotNull LocalDate date,
        @NotNull LocalDate dueDate,
        @NotNull @DecimalMin("0.00") BigDecimal remaining
) {}

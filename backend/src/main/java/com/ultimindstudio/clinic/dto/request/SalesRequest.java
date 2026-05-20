package com.ultimindstudio.clinic.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalesRequest(
        @NotNull Long staffId,
        @NotNull LocalDate date,
        @NotNull Long typePaymentId,
        @NotNull Long typeIncomeId,
        @NotNull Long clientId,
        @NotNull @DecimalMin("0.00") BigDecimal price,
        @NotBlank String description
) {}

package com.ultimindstudio.clinic.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HistoryInstallmentRequest(
        @NotNull Long clientId,
        @NotNull LocalDate date,
        @NotNull @DecimalMin("0.00") BigDecimal payment
) {}

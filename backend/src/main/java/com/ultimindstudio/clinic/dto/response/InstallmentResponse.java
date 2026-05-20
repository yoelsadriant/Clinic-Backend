package com.ultimindstudio.clinic.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentResponse(
        Long id,
        Long clientId,
        String clientName,
        BigDecimal total,
        LocalDate date,
        LocalDate dueDate,
        BigDecimal remaining
) {}

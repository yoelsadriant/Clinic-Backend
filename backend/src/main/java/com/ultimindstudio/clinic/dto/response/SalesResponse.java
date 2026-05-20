package com.ultimindstudio.clinic.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalesResponse(
        Long id,
        Long staffId,
        String staffName,
        LocalDate date,
        Long typePaymentId,
        String typePaymentName,
        Long typeIncomeId,
        String typeIncomeName,
        Long clientId,
        String clientName,
        BigDecimal price,
        String description
) {}

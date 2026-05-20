package com.ultimindstudio.clinic.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HistoryInstallmentResponse(
        Long id,
        Long clientId,
        String clientName,
        LocalDate date,
        BigDecimal payment
) {}

package com.ultimindstudio.clinic.dto.response;

import java.time.LocalDate;

public record HistoryResponse(
        Long id,
        LocalDate date,
        Long staffId,
        String staffName,
        Long clientPackageId,
        Long clientId,
        String clientName,
        Long treatmentId,
        String treatmentName,
        String note
) {}

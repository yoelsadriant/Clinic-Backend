package com.ultimindstudio.clinic.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record HistoryRequest(
        @NotNull LocalDate date,
        @NotNull Long staffId,
        @NotNull Long clientPackageId,
        @NotNull Long treatmentId,
        String note
) {}

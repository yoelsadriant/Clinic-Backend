package com.ultimindstudio.clinic.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleRequest(
        @NotNull Long clientId,
        @NotNull Long packageId,
        @NotNull Long treatmentId,
        @NotNull LocalDate date,
        @NotNull LocalTime time,
        @NotNull Long statusId
) {}

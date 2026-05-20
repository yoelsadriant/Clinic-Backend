package com.ultimindstudio.clinic.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleResponse(
        Long id,
        Long clientId,
        String clientName,
        Long packageId,
        String packageName,
        Long treatmentId,
        String treatmentName,
        LocalDate date,
        LocalTime time,
        Long statusId,
        String statusName
) {}

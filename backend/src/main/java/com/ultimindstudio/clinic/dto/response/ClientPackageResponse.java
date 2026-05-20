package com.ultimindstudio.clinic.dto.response;

public record ClientPackageResponse(
        Long id,
        Long clientId,
        String clientName,
        Long packageId,
        String packageName,
        Long scheduleId
) {}

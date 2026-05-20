package com.ultimindstudio.clinic.dto.response;

public record PackageDetailResponse(
        Long id,
        Long treatmentId,
        String treatmentName,
        Long packageId,
        String packageName,
        int quantity
) {}

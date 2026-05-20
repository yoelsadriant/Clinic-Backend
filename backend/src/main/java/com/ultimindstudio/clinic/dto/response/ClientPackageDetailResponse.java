package com.ultimindstudio.clinic.dto.response;

public record ClientPackageDetailResponse(
        Long id,
        Long clientPackageId,
        Long packageDetailId,
        String treatmentName,
        int quantity
) {}

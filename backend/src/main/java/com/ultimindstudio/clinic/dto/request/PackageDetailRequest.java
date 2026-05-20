package com.ultimindstudio.clinic.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PackageDetailRequest(
        @NotNull Long treatmentId,
        @NotNull Long packageId,
        @Min(1) int quantity
) {}

package com.ultimindstudio.clinic.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ClientPackageDetailRequest(
        @NotNull Long clientPackageId,
        @NotNull Long packageDetailId,
        @Min(1) int quantity
) {}

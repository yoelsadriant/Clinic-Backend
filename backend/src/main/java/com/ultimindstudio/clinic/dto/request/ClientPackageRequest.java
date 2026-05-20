package com.ultimindstudio.clinic.dto.request;

import jakarta.validation.constraints.NotNull;

public record ClientPackageRequest(
        @NotNull Long clientId,
        @NotNull Long packageId,
        @NotNull Long scheduleId
) {}

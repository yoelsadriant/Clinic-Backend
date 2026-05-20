package com.ultimindstudio.clinic.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BeauticianRequest(
        @NotNull Long staffId,
        @Min(0) int quantity
) {}

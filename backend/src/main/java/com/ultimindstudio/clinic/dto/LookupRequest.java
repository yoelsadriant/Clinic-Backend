package com.ultimindstudio.clinic.dto;

import jakarta.validation.constraints.NotBlank;

public record LookupRequest(@NotBlank String name) {}

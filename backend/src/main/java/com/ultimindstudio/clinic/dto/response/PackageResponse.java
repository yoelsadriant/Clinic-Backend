package com.ultimindstudio.clinic.dto.response;

import java.math.BigDecimal;

public record PackageResponse(Long id, String name, BigDecimal price) {}

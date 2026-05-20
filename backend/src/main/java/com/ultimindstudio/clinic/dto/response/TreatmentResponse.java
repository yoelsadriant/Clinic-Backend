package com.ultimindstudio.clinic.dto.response;

import java.math.BigDecimal;

public record TreatmentResponse(Long id, String name, BigDecimal price) {}

package com.ultimindstudio.clinic.dto.response;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, int quantity, BigDecimal price, String weight) {}

package com.ultimindstudio.clinic.dto.response;

public record BeauticianResponse(
        Long id,
        Long staffId,
        String staffName,
        int quantity
) {}

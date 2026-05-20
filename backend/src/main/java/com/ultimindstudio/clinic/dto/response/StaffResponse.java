package com.ultimindstudio.clinic.dto.response;

public record StaffResponse(
        Long id,
        String name,
        String address,
        String phone,
        String workplace,
        boolean active,
        Long occupationId,
        String occupationName
) {}

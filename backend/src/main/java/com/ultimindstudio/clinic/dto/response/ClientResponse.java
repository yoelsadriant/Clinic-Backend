package com.ultimindstudio.clinic.dto.response;

import java.time.LocalDate;

public record ClientResponse(
        Long id,
        String name,
        String sex,
        String address,
        String phone,
        String email,
        LocalDate registeredDate,
        Long staffId,
        String staffName
) {}

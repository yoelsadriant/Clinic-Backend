package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.response.SalesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ReportService {
    Page<SalesResponse> getRevenue(Long staffId, LocalDate from, LocalDate to, Pageable pageable);
    BigDecimal getTotalRevenue(Long staffId, LocalDate from, LocalDate to);
}

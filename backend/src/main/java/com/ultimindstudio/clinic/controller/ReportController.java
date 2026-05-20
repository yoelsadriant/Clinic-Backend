package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dto.response.SalesResponse;
import com.ultimindstudio.clinic.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/revenue")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Revenue report filtered by staff and/or date range")
    public ResponseEntity<Page<SalesResponse>> getRevenue(
            @RequestParam(required = false) Long staffId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Pageable pageable) {
        return ResponseEntity.ok(reportService.getRevenue(staffId, from, to, pageable));
    }

    @GetMapping("/revenue/total")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Total revenue sum filtered by staff and/or date range")
    public ResponseEntity<Map<String, BigDecimal>> getRevenueTotal(
            @RequestParam(required = false) Long staffId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        BigDecimal total = reportService.getTotalRevenue(staffId, from, to);
        return ResponseEntity.ok(Map.of("total", total));
    }
}

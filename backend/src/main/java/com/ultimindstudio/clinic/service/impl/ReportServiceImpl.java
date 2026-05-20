package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.response.SalesResponse;
import com.ultimindstudio.clinic.entity.Sales;
import com.ultimindstudio.clinic.repository.SalesRepository;
import com.ultimindstudio.clinic.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final SalesRepository salesRepository;

    @Override
    public Page<SalesResponse> getRevenue(Long staffId, LocalDate from, LocalDate to, Pageable pageable) {
        Page<Sales> page = (staffId != null)
                ? salesRepository.findByStaffIdAndDateBetween(staffId, from, to, pageable)
                : salesRepository.findByDateBetween(from, to, pageable);
        return page.map(this::toResponse);
    }

    @Override
    public BigDecimal getTotalRevenue(Long staffId, LocalDate from, LocalDate to) {
        return salesRepository.sumRevenue(staffId, from, to);
    }

    private SalesResponse toResponse(Sales s) {
        return new SalesResponse(s.getId(),
                s.getStaff().getId(), s.getStaff().getName(), s.getDate(),
                s.getTypePayment().getId(), s.getTypePayment().getName(),
                s.getTypeIncome().getId(), s.getTypeIncome().getName(),
                s.getClient().getId(), s.getClient().getName(),
                s.getPrice(), s.getDescription());
    }
}

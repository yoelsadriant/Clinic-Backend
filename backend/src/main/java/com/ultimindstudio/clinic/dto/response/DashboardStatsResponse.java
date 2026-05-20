package com.ultimindstudio.clinic.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record DashboardStatsResponse(
        long totalClients,
        long totalStaff,
        long activeStaff,
        long todayScheduleCount,
        BigDecimal currentMonthRevenue,
        List<ScheduleResponse> todaySchedules
) {}

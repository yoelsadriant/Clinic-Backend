package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.response.DashboardStatsResponse;
import com.ultimindstudio.clinic.dto.response.ScheduleResponse;
import com.ultimindstudio.clinic.entity.Schedule;
import com.ultimindstudio.clinic.repository.*;
import com.ultimindstudio.clinic.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ClientRepository clientRepository;
    private final StaffRepository staffRepository;
    private final ScheduleRepository scheduleRepository;
    private final SalesRepository salesRepository;

    @Override
    public DashboardStatsResponse getStats() {
        LocalDate today = LocalDate.now();
        YearMonth month = YearMonth.now();

        long totalClients = clientRepository.count();
        long totalStaff = staffRepository.count();
        long activeStaff = staffRepository.findByActiveTrue().size();

        List<Schedule> todayRaw = scheduleRepository.findByDate(today);
        List<ScheduleResponse> todaySchedules = todayRaw.stream().map(this::toScheduleResponse).toList();

        var monthRevenue = salesRepository.sumRevenue(null, month.atDay(1), month.atEndOfMonth());

        return new DashboardStatsResponse(
                totalClients, totalStaff, activeStaff,
                todaySchedules.size(), monthRevenue, todaySchedules
        );
    }

    private ScheduleResponse toScheduleResponse(Schedule s) {
        return new ScheduleResponse(
                s.getId(),
                s.getClient().getId(), s.getClient().getName(),
                s.getPkg().getId(), s.getPkg().getName(),
                s.getTreatment().getId(), s.getTreatment().getName(),
                s.getDate(), s.getTime(),
                s.getStatus().getId(), s.getStatus().getName()
        );
    }
}

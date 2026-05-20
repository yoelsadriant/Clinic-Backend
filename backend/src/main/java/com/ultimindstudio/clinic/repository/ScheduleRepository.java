package com.ultimindstudio.clinic.repository;

import com.ultimindstudio.clinic.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Page<Schedule> findByClientId(Long clientId, Pageable pageable);
    List<Schedule> findByDate(LocalDate date);
    List<Schedule> findByDateBetween(LocalDate from, LocalDate to);
    Page<Schedule> findByStatusId(Long statusId, Pageable pageable);
    boolean existsByClientIdAndDateAndTime(Long clientId, LocalDate date, LocalTime time);
}

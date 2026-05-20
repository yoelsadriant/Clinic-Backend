package com.ultimindstudio.clinic.repository;

import com.ultimindstudio.clinic.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HistoryRepository extends JpaRepository<History, Long> {
    Page<History> findByClientPackageClientId(Long clientId, Pageable pageable);
    Page<History> findByStaffId(Long staffId, Pageable pageable);
    Page<History> findByDateBetween(LocalDate from, LocalDate to, Pageable pageable);
}

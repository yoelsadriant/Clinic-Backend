package com.ultimindstudio.clinic.repository;

import com.ultimindstudio.clinic.entity.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SalesRepository extends JpaRepository<Sales, Long> {
    Page<Sales> findByClientId(Long clientId, Pageable pageable);
    Page<Sales> findByStaffId(Long staffId, Pageable pageable);
    Page<Sales> findByDateBetween(LocalDate from, LocalDate to, Pageable pageable);
    Page<Sales> findByStaffIdAndDateBetween(Long staffId, LocalDate from, LocalDate to, Pageable pageable);

    @Query("SELECT COALESCE(SUM(s.price), 0) FROM Sales s WHERE (:staffId IS NULL OR s.staff.id = :staffId) AND s.date BETWEEN :from AND :to")
    BigDecimal sumRevenue(@Param("staffId") Long staffId, @Param("from") LocalDate from, @Param("to") LocalDate to);
}

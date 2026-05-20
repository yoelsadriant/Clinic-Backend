package com.ultimindstudio.clinic.repository;

import com.ultimindstudio.clinic.entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InstallmentRepository extends JpaRepository<Installment, Long> {
    List<Installment> findByClientId(Long clientId);
    List<Installment> findByDueDateBefore(LocalDate date);
}

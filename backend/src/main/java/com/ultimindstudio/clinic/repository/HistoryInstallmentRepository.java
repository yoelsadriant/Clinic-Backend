package com.ultimindstudio.clinic.repository;

import com.ultimindstudio.clinic.entity.HistoryInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryInstallmentRepository extends JpaRepository<HistoryInstallment, Long> {
    List<HistoryInstallment> findByClientId(Long clientId);
}

package com.ultimindstudio.clinic.repository;

import com.ultimindstudio.clinic.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
}

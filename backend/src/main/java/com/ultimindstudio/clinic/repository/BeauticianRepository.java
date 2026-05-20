package com.ultimindstudio.clinic.repository;

import com.ultimindstudio.clinic.entity.Beautician;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BeauticianRepository extends JpaRepository<Beautician, Long> {
    Optional<Beautician> findByStaffId(Long staffId);
}

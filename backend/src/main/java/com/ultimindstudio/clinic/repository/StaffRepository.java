package com.ultimindstudio.clinic.repository;

import com.ultimindstudio.clinic.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByActiveTrue();
    List<Staff> findByOccupationId(Long occupationId);
}

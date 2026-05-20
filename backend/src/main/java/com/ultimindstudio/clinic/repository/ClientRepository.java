package com.ultimindstudio.clinic.repository;

import com.ultimindstudio.clinic.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Page<Client> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Client> findByStaffId(Long staffId, Pageable pageable);
}

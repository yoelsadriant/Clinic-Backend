package com.ultimindstudio.clinic.repository;

import com.ultimindstudio.clinic.entity.ClientPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientPackageRepository extends JpaRepository<ClientPackage, Long> {
    List<ClientPackage> findByClientId(Long clientId);
}

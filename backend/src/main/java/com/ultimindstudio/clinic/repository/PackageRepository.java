package com.ultimindstudio.clinic.repository;

import com.ultimindstudio.clinic.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Package, Long> {
}

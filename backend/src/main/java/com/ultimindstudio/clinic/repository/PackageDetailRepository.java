package com.ultimindstudio.clinic.repository;

import com.ultimindstudio.clinic.entity.PackageDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageDetailRepository extends JpaRepository<PackageDetail, Long> {
    List<PackageDetail> findByPkgId(Long packageId);
    List<PackageDetail> findByTreatmentId(Long treatmentId);
}

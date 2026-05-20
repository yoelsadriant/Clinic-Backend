package com.ultimindstudio.clinic.repository;

import com.ultimindstudio.clinic.entity.ClientPackageDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientPackageDetailRepository extends JpaRepository<ClientPackageDetail, Long> {
    List<ClientPackageDetail> findByClientPackageId(Long clientPackageId);
}

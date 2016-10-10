package com.ultimindstudio.clinic.dao;

import com.ultimindstudio.clinic.entity.PackageDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Yoel on 9/30/2016.
 */
public interface PackageDetailDao extends JpaRepository<PackageDetail, Long> {
}

package com.ultimindstudio.clinic.dao;

import com.ultimindstudio.clinic.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Yoel on 9/28/2016.
 */
public interface PackageDao extends JpaRepository<Package, Long> {
}

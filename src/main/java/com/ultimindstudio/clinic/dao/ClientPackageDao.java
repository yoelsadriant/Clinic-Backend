package com.ultimindstudio.clinic.dao;

import com.ultimindstudio.clinic.entity.ClientPackage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Yoel on 9/28/2016.
 */
public interface ClientPackageDao extends JpaRepository<ClientPackage, Long> {
}

package com.ultimindstudio.clinic.dao;

import com.ultimindstudio.clinic.entity.StatusClient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Yoel on 10/3/2016.
 */
public interface StatusClientDao extends JpaRepository<StatusClient, Long> {
}

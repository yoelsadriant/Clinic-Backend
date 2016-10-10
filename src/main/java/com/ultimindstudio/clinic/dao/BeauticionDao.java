package com.ultimindstudio.clinic.dao;

import com.ultimindstudio.clinic.entity.Beautician;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Yoel on 9/30/2016.
 */
public interface BeauticionDao extends JpaRepository<Beautician, Long> {
}

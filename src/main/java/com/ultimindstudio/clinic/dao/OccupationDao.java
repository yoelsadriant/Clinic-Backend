package com.ultimindstudio.clinic.dao;

import com.ultimindstudio.clinic.entity.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Yoel on 9/22/2016.
 */
public interface OccupationDao extends JpaRepository<Occupation, Long> {
}

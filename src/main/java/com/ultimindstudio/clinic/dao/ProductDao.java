package com.ultimindstudio.clinic.dao;

import com.ultimindstudio.clinic.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Yoel on 9/22/2016.
 */
public interface ProductDao extends JpaRepository<Product, Long> {
}

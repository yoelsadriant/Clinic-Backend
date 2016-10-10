package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.ProductDao;
import com.ultimindstudio.clinic.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/22/2016.
 */
@RestController
public class ProductController {
    @Autowired
    private ProductDao productDao;

    @RequestMapping(value = "/api/product", method = RequestMethod.GET)
    public Page<Product> findClientAll(Pageable page) {
        return productDao.findAll(page);
    }

    @RequestMapping(value = "/api/product", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertClient(@RequestBody Product product) {
        productDao.save(product);
    }

    @RequestMapping(value = "/api/product/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateClient(@PathVariable("id") Long id, @RequestBody Product product) {
        product.setId(id);
        productDao.save(product);
    }

    @RequestMapping(value = "/api/product/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Product findClientById(@PathVariable("id") Long id) {
        return productDao.findOne(id);
    }

    @RequestMapping(value = "/api/product/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable("id") Long id){
        productDao.delete(id);
    }
}

package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.SalesDao;
import com.ultimindstudio.clinic.entity.Sales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/22/2016.
 */
@RestController
public class SalesController {
    @Autowired
    private SalesDao salesDao;

    @RequestMapping(value = "/api/sales", method = RequestMethod.GET)
    public Page<Sales> findClientAll(Pageable page) {
        return salesDao.findAll(page);
    }

    @RequestMapping(value = "/api/sales", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertClient(@RequestBody Sales sales) {
        salesDao.save(sales);
    }

    @RequestMapping(value = "/api/sales/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateClient(@PathVariable("id") Long id, @RequestBody Sales sales) {
        sales.setId(id);
        salesDao.save(sales);
    }

    @RequestMapping(value = "/api/sales/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Sales findClientById(@PathVariable("id") Long id) {
        return salesDao.findOne(id);
    }

    @RequestMapping(value = "/api/sales/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSales(@PathVariable("id") Long id){
        salesDao.delete(id);
    }
}

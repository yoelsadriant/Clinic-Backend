package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.InstallmentDao;
import com.ultimindstudio.clinic.entity.Installment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/22/2016.
 */
@RestController
public class InstallmentController {
    @Autowired
    private InstallmentDao installmentDao;

    @RequestMapping(value = "/api/installment", method = RequestMethod.GET)
    public Page<Installment> findClientAll(Pageable page) {
        return installmentDao.findAll(page);
    }

    @RequestMapping(value = "/api/installment", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertClient(@RequestBody Installment installment) {
        installmentDao.save(installment);
    }

    @RequestMapping(value = "/api/installment/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateClient(@PathVariable("id") Long id, @RequestBody Installment installment) {
        installment.setId(id);
        installmentDao.save(installment);
    }

    @RequestMapping(value = "/api/installment/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Installment findClientById(@PathVariable("id") Long id) {
        return installmentDao.findOne(id);
    }

    @RequestMapping(value = "/api/installment/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteInstallment(@PathVariable("id") Long id){
        installmentDao.delete(id);
    }
}


package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.TypePaymentDao;
import com.ultimindstudio.clinic.entity.TypePayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 10/3/2016.
 */
@RestController
public class TypePaymentController {
    @Autowired
    private TypePaymentDao typeAppDao;

    @RequestMapping(value = "/api/typepayment", method = RequestMethod.GET)
    public Page<TypePayment> findTypePaymentAll(Pageable page) {
        return typeAppDao.findAll(page);
    }

    @RequestMapping(value = "/api/typepayment", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertTypePayment(@RequestBody TypePayment typeApp) {
        typeAppDao.save(typeApp);
    }

    @RequestMapping(value = "/api/typepayment/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateTypePayment(@PathVariable("id") Long id, @RequestBody TypePayment typeApp) {
        typeApp.setId(id);
        typeAppDao.save(typeApp);
    }

    @RequestMapping(value = "/api/typepayment/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public TypePayment findTypePaymentById(@PathVariable("id") Long id) {
        return typeAppDao.findOne(id);
    }

    @RequestMapping(value = "/api/typepayment/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteTypePayment(@PathVariable("id") Long id){
        typeAppDao.delete(id);
    }
}

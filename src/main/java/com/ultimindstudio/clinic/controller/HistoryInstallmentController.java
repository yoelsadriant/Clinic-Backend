package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.HistoryInstallmentDao;
import com.ultimindstudio.clinic.entity.HistoryInstallment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/22/2016.
 */
@RestController
public class HistoryInstallmentController {
    @Autowired
    private HistoryInstallmentDao historyInstallmentDao;

    @RequestMapping(value = "/api/historyinstallment", method = RequestMethod.GET)
    public Page<HistoryInstallment> findClientAll(Pageable page) {
        return historyInstallmentDao.findAll(page);
    }

    @RequestMapping(value = "/api/historyinstallment", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertClient(@RequestBody HistoryInstallment historyInstallment) {
        historyInstallmentDao.save(historyInstallment);
    }

    @RequestMapping(value = "/api/historyinstallment/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateClient(@PathVariable("id") Long id, @RequestBody HistoryInstallment historyInstallment) {
        historyInstallment.setId(id);
        historyInstallmentDao.save(historyInstallment);
    }

    @RequestMapping(value = "/api/historyinstallment/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public HistoryInstallment findClientById(@PathVariable("id") Long id) {
        return historyInstallmentDao.findOne(id);
    }

    @RequestMapping(value = "/api/historyinstallment/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteHistoryInstallment(@PathVariable("id") Long id){
        historyInstallmentDao.delete(id);
    }
}

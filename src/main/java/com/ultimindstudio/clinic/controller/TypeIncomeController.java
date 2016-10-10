package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.TypeIncomeDao;
import com.ultimindstudio.clinic.entity.TypeIncome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 10/3/2016.
 */
@RestController
public class TypeIncomeController {
    @Autowired
    private TypeIncomeDao typeIncomeDao;

    @RequestMapping(value = "/api/typeincome", method = RequestMethod.GET)
    public Page<TypeIncome> findTypeIncomeAll(Pageable page) {
        return typeIncomeDao.findAll(page);
    }

    @RequestMapping(value = "/api/typeincome", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertTypeIncome(@RequestBody TypeIncome typeApp) {
        typeIncomeDao.save(typeApp);
    }

    @RequestMapping(value = "/api/typeincome/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateTypeIncome(@PathVariable("id") Long id, @RequestBody TypeIncome typeApp) {
        typeApp.setId(id);
        typeIncomeDao.save(typeApp);
    }

    @RequestMapping(value = "/api/typeincome/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public TypeIncome findTypeIncomeById(@PathVariable("id") Long id) {
        return typeIncomeDao.findOne(id);
    }

    @RequestMapping(value = "/api/typeincome/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteTypeIncome(@PathVariable("id") Long id){
        typeIncomeDao.delete(id);
    }
}

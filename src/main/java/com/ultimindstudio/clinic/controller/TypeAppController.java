package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.TypeAppDao;
import com.ultimindstudio.clinic.entity.TypeApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/28/2016.
 */
@RestController
public class TypeAppController {
    @Autowired
    private TypeAppDao typeAppDao;

    @RequestMapping(value = "/api/typeapp", method = RequestMethod.GET)
    public Page<TypeApp> findTypeAppAll(Pageable page) {
        return typeAppDao.findAll(page);
    }

    @RequestMapping(value = "/api/typeapp", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertTypeApp(@RequestBody TypeApp typeApp) {
        typeAppDao.save(typeApp);
    }

    @RequestMapping(value = "/api/typeapp/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateTypeApp(@PathVariable("id") Long id, @RequestBody TypeApp typeApp) {
        typeApp.setId(id);
        typeAppDao.save(typeApp);
    }

    @RequestMapping(value = "/api/typeapp/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public TypeApp findTypeAppById(@PathVariable("id") Long id) {
        return typeAppDao.findOne(id);
    }

    @RequestMapping(value = "/api/typeapp/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteTypeApp(@PathVariable("id") Long id){
        typeAppDao.delete(id);
    }
}

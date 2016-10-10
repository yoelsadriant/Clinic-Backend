package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.LoginDao;
import com.ultimindstudio.clinic.entity.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/28/2016.
 */
@RestController
public class LoginController {

    @Autowired
    private LoginDao loginDao;

    @RequestMapping(value = "/api/login", method = RequestMethod.GET)
    public Page<Login> findLoginAll(Pageable page) {
        return loginDao.findAll(page);
    }

    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertLogin(@RequestBody Login login) {
        loginDao.save(login);
    }

    @RequestMapping(value = "/api/login/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateLogin(@PathVariable("id") Long id, @RequestBody Login login) {
        login.setId(id);
        loginDao.save(login);
    }

    @RequestMapping(value = "/api/login/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Login findLoginById(@PathVariable("id") Long id) {
        return loginDao.findOne(id);
    }

    @RequestMapping(value = "/api/login/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteLogin(@PathVariable("id") Long id) {
        loginDao.delete(id);
    }



}

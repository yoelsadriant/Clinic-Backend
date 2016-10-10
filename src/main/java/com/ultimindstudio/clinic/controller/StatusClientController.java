package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.StatusClientDao;
import com.ultimindstudio.clinic.entity.StatusClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 10/3/2016.
 */
@RestController
public class StatusClientController {
    @Autowired
    private StatusClientDao statusClientDao;

    @RequestMapping(value = "/api/statusclient", method = RequestMethod.GET)
    public Page<StatusClient> findClientAll(Pageable page) {
        return statusClientDao.findAll(page);
    }

    @RequestMapping(value = "/api/statusclient", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertStatusClient(@RequestBody StatusClient statusClient) {
        statusClientDao.save(statusClient);
    }

    @RequestMapping(value = "/api/statusclient/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateStatusClient(@PathVariable("id") Long id, @RequestBody StatusClient statusClient) {
        statusClient.setId(id);
        statusClientDao.save(statusClient);
    }

    @RequestMapping(value = "/api/statusclient/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public StatusClient findClientById(@PathVariable("id") Long id) {
        return statusClientDao.findOne(id);
    }

    @RequestMapping(value = "/api/statusclient/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteStatusClient(@PathVariable("id") Long id){
        statusClientDao.delete(id);
    }
}

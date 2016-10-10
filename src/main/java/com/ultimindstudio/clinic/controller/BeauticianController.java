package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.BeauticionDao;
import com.ultimindstudio.clinic.entity.Beautician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/30/2016.
 */
@RestController
public class BeauticianController {
    @Autowired
    private BeauticionDao beauticionDao;

    @RequestMapping(value = "/api/beautician", method = RequestMethod.GET)
    public Page<Beautician> findBeauticianAll(Pageable page) {
        return beauticionDao.findAll(page);
    }

    @RequestMapping(value = "/api/beautician", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertBeautician(@RequestBody Beautician beautician) {
        beauticionDao.save(beautician);
    }

    @RequestMapping(value = "/api/beautician/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateBeautician(@PathVariable("id") Long id, @RequestBody Beautician beautician) {
        beautician.setId(id);
        beauticionDao.save(beautician);
    }

    @RequestMapping(value = "/api/beautician/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Beautician findBeauticianById(@PathVariable("id") Long id) {
        return beauticionDao.findOne(id);
    }

    @RequestMapping(value = "/api/beautician/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteBeautician(@PathVariable("id") Long id){
        beauticionDao.delete(id);
    }
}

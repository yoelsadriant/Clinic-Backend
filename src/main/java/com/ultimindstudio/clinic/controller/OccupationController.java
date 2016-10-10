package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.OccupationDao;
import com.ultimindstudio.clinic.entity.Occupation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/22/2016.
 */
@RestController
public class OccupationController {
    @Autowired
    private OccupationDao occupationDao;

    @RequestMapping(value = "/api/occupation", method = RequestMethod.GET)
    public Page<Occupation> findClientAll(Pageable page) {
        return occupationDao.findAll(page);
    }

    @RequestMapping(value = "/api/occupation", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertClient(@RequestBody Occupation occupation) {
        occupationDao.save(occupation);
    }

    @RequestMapping(value = "/api/occupation/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateClient(@PathVariable("id") Long id, @RequestBody Occupation occupation) {
        occupation.setId(id);
        occupationDao.save(occupation);
    }

    @RequestMapping(value = "/api/occupation/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Occupation findClientById(@PathVariable("id") Long id) {
        return occupationDao.findOne(id);
    }

    @RequestMapping(value = "/api/occupation/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteOccupation(@PathVariable("id") Long id){
        occupationDao.delete(id);
    }
}

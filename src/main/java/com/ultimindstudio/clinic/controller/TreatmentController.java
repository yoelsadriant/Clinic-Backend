package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.TreatmentDao;
import com.ultimindstudio.clinic.entity.Treatment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/30/2016.
 */
@RestController
public class TreatmentController {

    @Autowired
    private TreatmentDao treatmentDao;

    @RequestMapping(value = "/api/treatment", method = RequestMethod.GET)
    public Page<Treatment> findTreatmentAll(Pageable page) {
        return treatmentDao.findAll(page);
    }

    @RequestMapping(value = "/api/treatment", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertTreatment(@RequestBody Treatment treatment) {
        treatmentDao.save(treatment);
    }

    @RequestMapping(value = "/api/treatment/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateTreatment(@PathVariable("id") Long id, @RequestBody Treatment treatment) {
        treatment.setId(id);
        treatmentDao.save(treatment);
    }

    @RequestMapping(value = "/api/treatment/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Treatment findTreatmentById(@PathVariable("id") Long id) {
        return treatmentDao.findOne(id);
    }

    @RequestMapping(value = "/api/treatment/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteTreatment(@PathVariable("id") Long id){
        treatmentDao.delete(id);
    }


}

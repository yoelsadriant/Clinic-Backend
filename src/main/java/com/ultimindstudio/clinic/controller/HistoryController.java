package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.HistoryDao;
import com.ultimindstudio.clinic.entity.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/22/2016.
 */
@RestController
public class HistoryController {
    @Autowired
    private HistoryDao historyDao;

    @RequestMapping(value = "/api/history", method = RequestMethod.GET)
    public Page<History> findClientAll(Pageable page) {
        return historyDao.findAll(page);
    }

    @RequestMapping(value = "/api/history", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertClient(@RequestBody History history) {
        historyDao.save(history);
    }

    @RequestMapping(value = "/api/history/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateClient(@PathVariable("id") Long id, @RequestBody History history) {
        history.setId(id);
        historyDao.save(history);
    }

    @RequestMapping(value = "/api/history/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public History findClientById(@PathVariable("id") Long id) {
        return historyDao.findOne(id);
    }

    @RequestMapping(value = "/api/history/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteHistory(@PathVariable("id") Long id){
        historyDao.delete(id);
    }

}

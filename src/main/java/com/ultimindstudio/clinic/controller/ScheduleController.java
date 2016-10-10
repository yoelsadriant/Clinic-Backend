package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.ScheduleDao;
import com.ultimindstudio.clinic.entity.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/22/2016.
 */
@RestController
public class ScheduleController {

    @Autowired
    private ScheduleDao scheduleDao;

    @RequestMapping(value = "/api/schedule", method = RequestMethod.GET)
    public Page<Schedule> findClientAll(Pageable page) {
        return scheduleDao.findAll(page);
    }

    @RequestMapping(value = "/api/schedule", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertClient(@RequestBody Schedule schedule) {
        scheduleDao.save(schedule);
    }

    @RequestMapping(value = "/api/schedule/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateClient(@PathVariable("id") Long id, @RequestBody Schedule schedule) {
        schedule.setId(id);
        scheduleDao.save(schedule);
    }

    @RequestMapping(value = "/api/schedule/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Schedule findClientById(@PathVariable("id") Long id) {
        return scheduleDao.findOne(id);
    }

    @RequestMapping(value = "/api/schedule/delete{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSchedule(@PathVariable("id") Long id){
        scheduleDao.delete(id);
    }
}

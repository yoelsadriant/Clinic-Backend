package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.NotificationDao;
import com.ultimindstudio.clinic.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Yoel on 10/6/2016.
 */
@RestController
public class NotificationController {

    @Autowired
    private NotificationDao notificationDao;

    @RequestMapping(value = "/api/notif", method = RequestMethod.GET)
    public Page<Notification> findClientAll(Pageable page) {
        return notificationDao.findAll(page);
    }

    @RequestMapping(value = "/api/notif", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertClient(@RequestBody Notification notification) {
        notificationDao.save(notification);
    }

    @RequestMapping(value = "/api/notif/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateClient(@PathVariable("id") Long id, @RequestBody Notification notification) {
        notification.setId(id);
        notificationDao.save(notification);
    }

    @RequestMapping(value = "/api/notif/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Notification findClientById(@PathVariable("id") Long id) {
        return notificationDao.findOne(id);
    }

    @RequestMapping(value = "/api/notif/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteClient(@PathVariable("id") Long id){
        notificationDao.delete(id);
    }

    @RequestMapping(value = "/api/notif/date/{datetime}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> findByDate(@PathVariable("datetime") String datetime){
        return notificationDao.findByDate(datetime);
    }


}

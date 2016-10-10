package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.StaffDao;
import com.ultimindstudio.clinic.entity.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/22/2016.
 */
@RestController
public class StaffController {
    @Autowired
    private StaffDao staffDao;

    @RequestMapping(value = "/api/staff", method = RequestMethod.GET)
    public Page<Staff> findStaffAll(Pageable page) {
        return staffDao.findAll(page);
    }

    @RequestMapping(value = "/api/staff", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertStaff(@RequestBody Staff staff) {
        staffDao.save(staff);
    }

    @RequestMapping(value = "/api/staff/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateStaff(@PathVariable("id") Long id, @RequestBody Staff staff) {
        staff.setId(id);
        staffDao.save(staff);
    }

    @RequestMapping(value = "/api/staff/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Staff findStaffById(@PathVariable("id") Long id) {
        return staffDao.findOne(id);
    }

    @RequestMapping(value = "/api/staff/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteStaff(@PathVariable("id") Long id){
        staffDao.delete(id);
    }
}

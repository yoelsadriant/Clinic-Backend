package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.PackageDao;
import com.ultimindstudio.clinic.entity.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Yoel on 9/28/2016.
 */
@RestController
public class PackageController {
    @Autowired
    private PackageDao packageDao;

    @RequestMapping(value = "/api/package", method = RequestMethod.GET)
    public Page<Package> findPackageAll(Pageable page) {
        return packageDao.findAll(page);
    }

    @RequestMapping(value = "/api/package", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertPackage(@RequestBody Package pkg) {
        packageDao.save(pkg);
    }

    @RequestMapping(value = "/api/package/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updatePackage(@PathVariable("id") Long id, @RequestBody Package pkg) {
        pkg.setId(id);
        packageDao.save(pkg);
    }

    @RequestMapping(value = "/api/package/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Package findPackageById(@PathVariable("id") Long id) {
        return packageDao.findOne(id);
    }

    @RequestMapping(value = "/api/package/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deletePackage(@PathVariable("id") Long id) {
        packageDao.delete(id);
    }

}

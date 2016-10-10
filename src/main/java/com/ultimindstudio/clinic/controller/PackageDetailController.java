package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.PackageDetailDao;
import com.ultimindstudio.clinic.entity.PackageDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/30/2016.
 */
@RestController
public class PackageDetailController {
    @Autowired
    private PackageDetailDao packageDetailDao;

    @RequestMapping(value = "/api/packagedetail", method = RequestMethod.GET)
    public Page<PackageDetail> findPackageAll(Pageable page) {
        return packageDetailDao.findAll(page);
    }

    @RequestMapping(value = "/api/packagedetail", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertPackage(@RequestBody PackageDetail pkg) {
        packageDetailDao.save(pkg);
    }

    @RequestMapping(value = "/api/packagedetail/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updatePackage(@PathVariable("id") Long id, @RequestBody PackageDetail pkg) {
        pkg.setId(id);
        packageDetailDao.save(pkg);
    }

    @RequestMapping(value = "/api/packagedetail/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public PackageDetail findPackageById(@PathVariable("id") Long id) {
        return packageDetailDao.findOne(id);
    }

    @RequestMapping(value = "/api/packagedetail/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deletePackage(@PathVariable("id") Long id) {
        packageDetailDao.delete(id);
    }
}

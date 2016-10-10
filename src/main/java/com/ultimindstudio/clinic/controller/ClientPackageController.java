package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.ClientPackageDao;
import com.ultimindstudio.clinic.entity.ClientPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/28/2016.
 */
@RestController
public class ClientPackageController {

    @Autowired
    private ClientPackageDao clientPackageDao;

    @RequestMapping(value = "/api/clientpackage", method = RequestMethod.GET)
    public Page<ClientPackage> findClientPackageAll(Pageable page){
        return clientPackageDao.findAll(page);
    }

    @RequestMapping(value = "/api/clientpackage", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertClientPackage(@RequestBody ClientPackage clientPackage) {
        clientPackageDao.save(clientPackage);
    }

    @RequestMapping(value = "/api/clientpackage/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateClientPackage(@PathVariable("id") Long id, @RequestBody ClientPackage clientPackage) {
        clientPackage.setId(id);
        clientPackageDao.save(clientPackage);
    }

    @RequestMapping(value = "/api/clientpackage/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ClientPackage findClientPackageById(@PathVariable("id") Long id) {
        return clientPackageDao.findOne(id);
    }

    @RequestMapping(value = "/api/clientpackage/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteClientPackage(@PathVariable("id") Long id){
        clientPackageDao.delete(id);
    }
}

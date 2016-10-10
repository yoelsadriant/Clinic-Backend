package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.ClientPackageDetailDao;
import com.ultimindstudio.clinic.entity.ClientPackageDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/30/2016.
 */
@RestController
public class ClientPackageDetailController {

    @Autowired
    private ClientPackageDetailDao clientPackageDetailDao;

    @RequestMapping(value = "/api/clientpackagedetail", method = RequestMethod.GET)
    public Page<ClientPackageDetail> findClientPackageDetailAll(Pageable page) {
        return clientPackageDetailDao.findAll(page);
    }

    @RequestMapping(value = "/api/clientpackagedetail", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertClientPackageDetail(@RequestBody ClientPackageDetail client) {
        clientPackageDetailDao.save(client);
    }

    @RequestMapping(value = "/api/clientpackagedetail/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateClientPackageDetail(@PathVariable("id") Long id, @RequestBody ClientPackageDetail client) {
        client.setId(id);
        clientPackageDetailDao.save(client);
    }

    @RequestMapping(value = "/api/clientpackagedetail/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ClientPackageDetail findClientPackageDetailById(@PathVariable("id") Long id) {
        return clientPackageDetailDao.findOne(id);
    }

    @RequestMapping(value = "/api/clientpackagedetail/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteClientPackageDetail(@PathVariable("id") Long id){
        clientPackageDetailDao.delete(id);
    }


}

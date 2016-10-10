package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dao.ClientDao;
import com.ultimindstudio.clinic.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yoel on 9/22/2016.
 */
@RestController
public class ClientController {

    @Autowired
    private ClientDao clientDao;

    @RequestMapping(value = "/api/client", method = RequestMethod.GET)
    public Page<Client> findClientAll(Pageable page) {
        return clientDao.findAll(page);
    }

    @RequestMapping(value = "/api/client", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertClient(@RequestBody Client client) {
        clientDao.save(client);
    }

    @RequestMapping(value = "/api/client/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateClient(@PathVariable("id") Long id, @RequestBody Client client) {
        client.setId(id);
        clientDao.save(client);
    }

    @RequestMapping(value = "/api/client/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Client findClientById(@PathVariable("id") Long id) {
        return clientDao.findOne(id);
    }

    @RequestMapping(value = "/api/client/delete/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void deleteClient(@PathVariable("id") Long id){
        clientDao.delete(id);
    }


}

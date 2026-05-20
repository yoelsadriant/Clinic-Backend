package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.ClientRequest;
import com.ultimindstudio.clinic.dto.response.ClientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {
    Page<ClientResponse> findAll(Pageable pageable);
    ClientResponse findById(Long id);
    ClientResponse create(ClientRequest request);
    ClientResponse update(Long id, ClientRequest request);
    void delete(Long id);
}

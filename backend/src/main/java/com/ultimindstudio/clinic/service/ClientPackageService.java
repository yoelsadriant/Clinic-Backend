package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.ClientPackageRequest;
import com.ultimindstudio.clinic.dto.response.ClientPackageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientPackageService {
    Page<ClientPackageResponse> findAll(Pageable pageable);
    ClientPackageResponse findById(Long id);
    ClientPackageResponse create(ClientPackageRequest request);
    ClientPackageResponse update(Long id, ClientPackageRequest request);
    void delete(Long id);
}

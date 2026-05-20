package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.ClientPackageDetailRequest;
import com.ultimindstudio.clinic.dto.response.ClientPackageDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientPackageDetailService {
    Page<ClientPackageDetailResponse> findAll(Pageable pageable);
    ClientPackageDetailResponse findById(Long id);
    ClientPackageDetailResponse create(ClientPackageDetailRequest request);
    ClientPackageDetailResponse update(Long id, ClientPackageDetailRequest request);
    void delete(Long id);
}

package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.PackageRequest;
import com.ultimindstudio.clinic.dto.response.PackageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PackageService {
    Page<PackageResponse> findAll(Pageable pageable);
    PackageResponse findById(Long id);
    PackageResponse create(PackageRequest request);
    PackageResponse update(Long id, PackageRequest request);
    void delete(Long id);
}

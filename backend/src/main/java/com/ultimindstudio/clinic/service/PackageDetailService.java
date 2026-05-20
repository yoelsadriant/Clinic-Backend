package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.PackageDetailRequest;
import com.ultimindstudio.clinic.dto.response.PackageDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PackageDetailService {
    Page<PackageDetailResponse> findAll(Pageable pageable);
    PackageDetailResponse findById(Long id);
    PackageDetailResponse create(PackageDetailRequest request);
    PackageDetailResponse update(Long id, PackageDetailRequest request);
    void delete(Long id);
}

package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.PackageRequest;
import com.ultimindstudio.clinic.dto.response.PackageResponse;
import com.ultimindstudio.clinic.entity.Package;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.PackageRepository;
import com.ultimindstudio.clinic.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;

    @Override @Transactional(readOnly = true)
    public Page<PackageResponse> findAll(Pageable pageable) {
        return packageRepository.findAll(pageable).map(p -> new PackageResponse(p.getId(), p.getName(), p.getPrice()));
    }

    @Override @Transactional(readOnly = true)
    public PackageResponse findById(Long id) {
        Package p = getById(id); return new PackageResponse(p.getId(), p.getName(), p.getPrice());
    }

    @Override
    public PackageResponse create(PackageRequest req) {
        Package p = Package.builder().name(req.name()).price(req.price()).build();
        Package saved = packageRepository.save(p);
        return new PackageResponse(saved.getId(), saved.getName(), saved.getPrice());
    }

    @Override
    public PackageResponse update(Long id, PackageRequest req) {
        Package p = getById(id); p.setName(req.name()); p.setPrice(req.price());
        Package saved = packageRepository.save(p);
        return new PackageResponse(saved.getId(), saved.getName(), saved.getPrice());
    }

    @Override
    public void delete(Long id) { getById(id); packageRepository.deleteById(id); }

    private Package getById(Long id) {
        return packageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Package", "id", id));
    }
}

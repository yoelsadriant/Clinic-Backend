package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.PackageDetailRequest;
import com.ultimindstudio.clinic.dto.response.PackageDetailResponse;
import com.ultimindstudio.clinic.entity.Package;
import com.ultimindstudio.clinic.entity.PackageDetail;
import com.ultimindstudio.clinic.entity.Treatment;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.PackageDetailRepository;
import com.ultimindstudio.clinic.repository.PackageRepository;
import com.ultimindstudio.clinic.repository.TreatmentRepository;
import com.ultimindstudio.clinic.service.PackageDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PackageDetailServiceImpl implements PackageDetailService {

    private final PackageDetailRepository packageDetailRepository;
    private final PackageRepository packageRepository;
    private final TreatmentRepository treatmentRepository;

    @Override @Transactional(readOnly = true)
    public Page<PackageDetailResponse> findAll(Pageable pageable) {
        return packageDetailRepository.findAll(pageable).map(this::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public PackageDetailResponse findById(Long id) { return toResponse(getById(id)); }

    @Override
    public PackageDetailResponse create(PackageDetailRequest req) {
        PackageDetail pd = PackageDetail.builder()
                .treatment(findTreatment(req.treatmentId()))
                .pkg(findPackage(req.packageId()))
                .quantity(req.quantity()).build();
        return toResponse(packageDetailRepository.save(pd));
    }

    @Override
    public PackageDetailResponse update(Long id, PackageDetailRequest req) {
        PackageDetail pd = getById(id);
        pd.setTreatment(findTreatment(req.treatmentId()));
        pd.setPkg(findPackage(req.packageId()));
        pd.setQuantity(req.quantity());
        return toResponse(packageDetailRepository.save(pd));
    }

    @Override
    public void delete(Long id) { getById(id); packageDetailRepository.deleteById(id); }

    private PackageDetail getById(Long id) {
        return packageDetailRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PackageDetail", "id", id));
    }
    private Treatment findTreatment(Long id) {
        return treatmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Treatment", "id", id));
    }
    private Package findPackage(Long id) {
        return packageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Package", "id", id));
    }

    private PackageDetailResponse toResponse(PackageDetail pd) {
        return new PackageDetailResponse(pd.getId(),
                pd.getTreatment().getId(), pd.getTreatment().getName(),
                pd.getPkg().getId(), pd.getPkg().getName(), pd.getQuantity());
    }
}

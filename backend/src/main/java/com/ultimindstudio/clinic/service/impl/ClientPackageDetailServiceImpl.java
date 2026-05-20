package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.ClientPackageDetailRequest;
import com.ultimindstudio.clinic.dto.response.ClientPackageDetailResponse;
import com.ultimindstudio.clinic.entity.ClientPackage;
import com.ultimindstudio.clinic.entity.ClientPackageDetail;
import com.ultimindstudio.clinic.entity.PackageDetail;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.ClientPackageDetailRepository;
import com.ultimindstudio.clinic.repository.ClientPackageRepository;
import com.ultimindstudio.clinic.repository.PackageDetailRepository;
import com.ultimindstudio.clinic.service.ClientPackageDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientPackageDetailServiceImpl implements ClientPackageDetailService {

    private final ClientPackageDetailRepository repository;
    private final ClientPackageRepository clientPackageRepository;
    private final PackageDetailRepository packageDetailRepository;

    @Override @Transactional(readOnly = true)
    public Page<ClientPackageDetailResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public ClientPackageDetailResponse findById(Long id) { return toResponse(getById(id)); }

    @Override
    public ClientPackageDetailResponse create(ClientPackageDetailRequest req) {
        ClientPackageDetail cpd = ClientPackageDetail.builder()
                .clientPackage(findClientPackage(req.clientPackageId()))
                .packageDetail(findPackageDetail(req.packageDetailId()))
                .quantity(req.quantity()).build();
        return toResponse(repository.save(cpd));
    }

    @Override
    public ClientPackageDetailResponse update(Long id, ClientPackageDetailRequest req) {
        ClientPackageDetail cpd = getById(id);
        cpd.setClientPackage(findClientPackage(req.clientPackageId()));
        cpd.setPackageDetail(findPackageDetail(req.packageDetailId()));
        cpd.setQuantity(req.quantity());
        return toResponse(repository.save(cpd));
    }

    @Override
    public void delete(Long id) { getById(id); repository.deleteById(id); }

    private ClientPackageDetail getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ClientPackageDetail", "id", id));
    }
    private ClientPackage findClientPackage(Long id) {
        return clientPackageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ClientPackage", "id", id));
    }
    private PackageDetail findPackageDetail(Long id) {
        return packageDetailRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PackageDetail", "id", id));
    }

    private ClientPackageDetailResponse toResponse(ClientPackageDetail cpd) {
        return new ClientPackageDetailResponse(cpd.getId(),
                cpd.getClientPackage().getId(),
                cpd.getPackageDetail().getId(),
                cpd.getPackageDetail().getTreatment().getName(),
                cpd.getQuantity());
    }
}

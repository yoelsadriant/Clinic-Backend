package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.ClientPackageRequest;
import com.ultimindstudio.clinic.dto.response.ClientPackageResponse;
import com.ultimindstudio.clinic.entity.Client;
import com.ultimindstudio.clinic.entity.ClientPackage;
import com.ultimindstudio.clinic.entity.Package;
import com.ultimindstudio.clinic.entity.Schedule;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.ClientPackageRepository;
import com.ultimindstudio.clinic.repository.ClientRepository;
import com.ultimindstudio.clinic.repository.PackageRepository;
import com.ultimindstudio.clinic.repository.ScheduleRepository;
import com.ultimindstudio.clinic.service.ClientPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientPackageServiceImpl implements ClientPackageService {

    private final ClientPackageRepository clientPackageRepository;
    private final ClientRepository clientRepository;
    private final PackageRepository packageRepository;
    private final ScheduleRepository scheduleRepository;

    @Override @Transactional(readOnly = true)
    public Page<ClientPackageResponse> findAll(Pageable pageable) {
        return clientPackageRepository.findAll(pageable).map(this::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public ClientPackageResponse findById(Long id) { return toResponse(getById(id)); }

    @Override
    public ClientPackageResponse create(ClientPackageRequest req) {
        ClientPackage cp = ClientPackage.builder()
                .client(findClient(req.clientId()))
                .pkg(findPackage(req.packageId()))
                .schedule(findSchedule(req.scheduleId())).build();
        return toResponse(clientPackageRepository.save(cp));
    }

    @Override
    public ClientPackageResponse update(Long id, ClientPackageRequest req) {
        ClientPackage cp = getById(id);
        cp.setClient(findClient(req.clientId()));
        cp.setPkg(findPackage(req.packageId()));
        cp.setSchedule(findSchedule(req.scheduleId()));
        return toResponse(clientPackageRepository.save(cp));
    }

    @Override
    public void delete(Long id) { getById(id); clientPackageRepository.deleteById(id); }

    private ClientPackage getById(Long id) {
        return clientPackageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ClientPackage", "id", id));
    }
    private Client findClient(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
    }
    private Package findPackage(Long id) {
        return packageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Package", "id", id));
    }
    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Schedule", "id", id));
    }

    private ClientPackageResponse toResponse(ClientPackage cp) {
        return new ClientPackageResponse(cp.getId(),
                cp.getClient().getId(), cp.getClient().getName(),
                cp.getPkg().getId(), cp.getPkg().getName(),
                cp.getSchedule().getId());
    }
}

package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.ScheduleRequest;
import com.ultimindstudio.clinic.dto.response.ScheduleResponse;
import com.ultimindstudio.clinic.entity.*;
import com.ultimindstudio.clinic.entity.Package;
import com.ultimindstudio.clinic.exception.BusinessException;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.*;
import com.ultimindstudio.clinic.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ClientRepository clientRepository;
    private final PackageRepository packageRepository;
    private final TreatmentRepository treatmentRepository;
    private final StatusClientRepository statusRepository;

    @Override @Transactional(readOnly = true)
    public Page<ScheduleResponse> findAll(Pageable pageable) {
        return scheduleRepository.findAll(pageable).map(this::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public ScheduleResponse findById(Long id) { return toResponse(getById(id)); }

    @Override
    public ScheduleResponse create(ScheduleRequest req) {
        if (scheduleRepository.existsByClientIdAndDateAndTime(req.clientId(), req.date(), req.time())) {
            throw new BusinessException("Client already has an appointment at " + req.date() + " " + req.time());
        }
        Schedule schedule = Schedule.builder()
                .client(findClient(req.clientId()))
                .pkg(findPackage(req.packageId()))
                .treatment(findTreatment(req.treatmentId()))
                .date(req.date()).time(req.time())
                .status(findStatus(req.statusId())).build();
        return toResponse(scheduleRepository.save(schedule));
    }

    @Override
    public ScheduleResponse update(Long id, ScheduleRequest req) {
        Schedule s = getById(id);
        s.setClient(findClient(req.clientId())); s.setPkg(findPackage(req.packageId()));
        s.setTreatment(findTreatment(req.treatmentId())); s.setDate(req.date());
        s.setTime(req.time()); s.setStatus(findStatus(req.statusId()));
        return toResponse(scheduleRepository.save(s));
    }

    @Override
    public void delete(Long id) { getById(id); scheduleRepository.deleteById(id); }

    private Schedule getById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Schedule", "id", id));
    }
    private Client findClient(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
    }
    private Package findPackage(Long id) {
        return packageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Package", "id", id));
    }
    private Treatment findTreatment(Long id) {
        return treatmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Treatment", "id", id));
    }
    private StatusClient findStatus(Long id) {
        return statusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Status", "id", id));
    }

    private ScheduleResponse toResponse(Schedule s) {
        return new ScheduleResponse(s.getId(),
                s.getClient().getId(), s.getClient().getName(),
                s.getPkg().getId(), s.getPkg().getName(),
                s.getTreatment().getId(), s.getTreatment().getName(),
                s.getDate(), s.getTime(),
                s.getStatus().getId(), s.getStatus().getName());
    }
}

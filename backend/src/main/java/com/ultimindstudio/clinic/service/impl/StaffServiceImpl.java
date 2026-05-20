package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.StaffRequest;
import com.ultimindstudio.clinic.dto.response.StaffResponse;
import com.ultimindstudio.clinic.entity.Occupation;
import com.ultimindstudio.clinic.entity.Staff;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.OccupationRepository;
import com.ultimindstudio.clinic.repository.StaffRepository;
import com.ultimindstudio.clinic.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final OccupationRepository occupationRepository;

    @Override @Transactional(readOnly = true)
    public Page<StaffResponse> findAll(Pageable pageable) {
        return staffRepository.findAll(pageable).map(this::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public StaffResponse findById(Long id) { return toResponse(getById(id)); }

    @Override
    public StaffResponse create(StaffRequest req) {
        Occupation occ = occupationRepository.findById(req.occupationId())
                .orElseThrow(() -> new ResourceNotFoundException("Occupation", "id", req.occupationId()));
        Staff staff = Staff.builder().name(req.name()).address(req.address()).phone(req.phone())
                .workplace(req.workplace()).active(req.active()).occupation(occ).build();
        return toResponse(staffRepository.save(staff));
    }

    @Override
    public StaffResponse update(Long id, StaffRequest req) {
        Staff staff = getById(id);
        Occupation occ = occupationRepository.findById(req.occupationId())
                .orElseThrow(() -> new ResourceNotFoundException("Occupation", "id", req.occupationId()));
        staff.setName(req.name()); staff.setAddress(req.address()); staff.setPhone(req.phone());
        staff.setWorkplace(req.workplace()); staff.setActive(req.active()); staff.setOccupation(occ);
        return toResponse(staffRepository.save(staff));
    }

    @Override
    public void delete(Long id) { getById(id); staffRepository.deleteById(id); }

    private Staff getById(Long id) {
        return staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));
    }

    private StaffResponse toResponse(Staff s) {
        return new StaffResponse(s.getId(), s.getName(), s.getAddress(), s.getPhone(),
                s.getWorkplace(), s.isActive(), s.getOccupation().getId(), s.getOccupation().getName());
    }
}

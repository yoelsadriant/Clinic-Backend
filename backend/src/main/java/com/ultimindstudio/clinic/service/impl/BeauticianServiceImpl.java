package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.BeauticianRequest;
import com.ultimindstudio.clinic.dto.response.BeauticianResponse;
import com.ultimindstudio.clinic.entity.Beautician;
import com.ultimindstudio.clinic.entity.Staff;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.BeauticianRepository;
import com.ultimindstudio.clinic.repository.StaffRepository;
import com.ultimindstudio.clinic.service.BeauticianService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BeauticianServiceImpl implements BeauticianService {

    private final BeauticianRepository beauticianRepository;
    private final StaffRepository staffRepository;

    @Override @Transactional(readOnly = true)
    public Page<BeauticianResponse> findAll(Pageable pageable) {
        return beauticianRepository.findAll(pageable).map(this::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public BeauticianResponse findById(Long id) { return toResponse(getById(id)); }

    @Override
    public BeauticianResponse create(BeauticianRequest req) {
        Staff staff = findStaff(req.staffId());
        Beautician b = Beautician.builder().staff(staff).quantity(req.quantity()).build();
        return toResponse(beauticianRepository.save(b));
    }

    @Override
    public BeauticianResponse update(Long id, BeauticianRequest req) {
        Beautician b = getById(id);
        b.setStaff(findStaff(req.staffId())); b.setQuantity(req.quantity());
        return toResponse(beauticianRepository.save(b));
    }

    @Override
    public void delete(Long id) { getById(id); beauticianRepository.deleteById(id); }

    private Beautician getById(Long id) {
        return beauticianRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Beautician", "id", id));
    }
    private Staff findStaff(Long id) {
        return staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));
    }

    private BeauticianResponse toResponse(Beautician b) {
        return new BeauticianResponse(b.getId(), b.getStaff().getId(), b.getStaff().getName(), b.getQuantity());
    }
}

package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.ClientRequest;
import com.ultimindstudio.clinic.dto.response.ClientResponse;
import com.ultimindstudio.clinic.entity.Client;
import com.ultimindstudio.clinic.entity.Staff;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.ClientRepository;
import com.ultimindstudio.clinic.repository.StaffRepository;
import com.ultimindstudio.clinic.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final StaffRepository staffRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ClientResponse> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponse findById(Long id) {
        return toResponse(getById(id));
    }

    @Override
    public ClientResponse create(ClientRequest req) {
        Staff staff = staffRepository.findById(req.staffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", req.staffId()));
        Client client = Client.builder()
                .name(req.name()).sex(req.sex()).address(req.address())
                .phone(req.phone()).email(req.email())
                .registeredDate(req.registeredDate()).staff(staff).build();
        return toResponse(clientRepository.save(client));
    }

    @Override
    public ClientResponse update(Long id, ClientRequest req) {
        Client client = getById(id);
        Staff staff = staffRepository.findById(req.staffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", req.staffId()));
        client.setName(req.name()); client.setSex(req.sex()); client.setAddress(req.address());
        client.setPhone(req.phone()); client.setEmail(req.email());
        client.setRegisteredDate(req.registeredDate()); client.setStaff(staff);
        return toResponse(clientRepository.save(client));
    }

    @Override
    public void delete(Long id) {
        getById(id);
        clientRepository.deleteById(id);
    }

    private Client getById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
    }

    private ClientResponse toResponse(Client c) {
        return new ClientResponse(c.getId(), c.getName(), c.getSex(), c.getAddress(),
                c.getPhone(), c.getEmail(), c.getRegisteredDate(),
                c.getStaff().getId(), c.getStaff().getName());
    }
}

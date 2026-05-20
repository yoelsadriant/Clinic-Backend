package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.ClientRequest;
import com.ultimindstudio.clinic.dto.response.ClientResponse;
import com.ultimindstudio.clinic.entity.Client;
import com.ultimindstudio.clinic.entity.Occupation;
import com.ultimindstudio.clinic.entity.Staff;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.ClientRepository;
import com.ultimindstudio.clinic.repository.StaffRepository;
import com.ultimindstudio.clinic.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock ClientRepository clientRepository;
    @Mock StaffRepository staffRepository;
    @InjectMocks ClientServiceImpl clientService;

    Staff staff;
    Client client;

    @BeforeEach
    void setUp() {
        Occupation occ = Occupation.builder().id(1L).name("Doctor").build();
        staff = Staff.builder().id(1L).name("Dr. Smith").address("Clinic").phone("555-0000")
                .workplace("Main Branch").active(true).occupation(occ).build();
        client = Client.builder().id(1L).name("John Doe").sex("M").address("123 Main St")
                .phone("555-1234").email("john@example.com")
                .registeredDate(LocalDate.of(2024, 1, 15)).staff(staff).build();
    }

    @Test
    void findAll_returnsPageOfClients() {
        when(clientRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(client)));
        var result = clientService.findAll(Pageable.unpaged());
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("John Doe");
    }

    @Test
    void findById_existingId_returnsClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        ClientResponse response = clientService.findById(1L);
        assertThat(response.name()).isEqualTo("John Doe");
        assertThat(response.staffName()).isEqualTo("Dr. Smith");
    }

    @Test
    void findById_unknownId_throwsResourceNotFoundException() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clientService.findById(99L));
    }

    @Test
    void create_validRequest_savesAndReturnsClient() {
        ClientRequest req = new ClientRequest("Jane Doe", "F", "456 Oak Ave",
                "555-5678", "jane@example.com", LocalDate.now(), 1L);
        Client saved = Client.builder().id(2L).name("Jane Doe").sex("F").address("456 Oak Ave")
                .phone("555-5678").email("jane@example.com").registeredDate(LocalDate.now()).staff(staff).build();
        when(staffRepository.findById(1L)).thenReturn(Optional.of(staff));
        when(clientRepository.save(any(Client.class))).thenReturn(saved);

        ClientResponse response = clientService.create(req);

        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.name()).isEqualTo("Jane Doe");
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void create_unknownStaff_throwsResourceNotFoundException() {
        ClientRequest req = new ClientRequest("Jane", "F", "Addr", "555", "j@e.com", LocalDate.now(), 99L);
        when(staffRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clientService.create(req));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void update_existingClient_updatesFields() {
        ClientRequest req = new ClientRequest("John Updated", "M", "New Address",
                "555-9999", "john.new@example.com", LocalDate.now(), 1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(staffRepository.findById(1L)).thenReturn(Optional.of(staff));
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

        ClientResponse response = clientService.update(1L, req);

        assertThat(response.name()).isEqualTo("John Updated");
        assertThat(response.email()).isEqualTo("john.new@example.com");
    }

    @Test
    void delete_existingClient_deletesById() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        clientService.delete(1L);
        verify(clientRepository).deleteById(1L);
    }

    @Test
    void delete_unknownId_throwsResourceNotFoundException() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clientService.delete(99L));
        verify(clientRepository, never()).deleteById(any());
    }
}

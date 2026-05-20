package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.ScheduleRequest;
import com.ultimindstudio.clinic.dto.response.ScheduleResponse;
import com.ultimindstudio.clinic.entity.*;
import com.ultimindstudio.clinic.entity.Package;
import com.ultimindstudio.clinic.exception.BusinessException;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.*;
import com.ultimindstudio.clinic.service.impl.ScheduleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {

    @Mock ScheduleRepository scheduleRepository;
    @Mock ClientRepository clientRepository;
    @Mock PackageRepository packageRepository;
    @Mock TreatmentRepository treatmentRepository;
    @Mock StatusClientRepository statusRepository;
    @InjectMocks ScheduleServiceImpl scheduleService;

    Client client;
    Package pkg;
    Treatment treatment;
    StatusClient status;
    Schedule schedule;

    @BeforeEach
    void setUp() {
        Occupation occ = Occupation.builder().id(1L).name("Therapist").build();
        Staff staff = Staff.builder().id(1L).name("Dr. Lee").address("Clinic")
                .phone("555-0000").workplace("Main").active(true).occupation(occ).build();
        client = Client.builder().id(1L).name("Alice").sex("F").address("Addr")
                .phone("555").email("a@b.com").registeredDate(LocalDate.now()).staff(staff).build();
        pkg = Package.builder().id(1L).name("Basic Package").price(BigDecimal.valueOf(100)).build();
        treatment = Treatment.builder().id(1L).name("Facial").price(BigDecimal.valueOf(50)).build();
        status = StatusClient.builder().id(1L).name("Booked").build();
        schedule = Schedule.builder().id(1L).client(client).pkg(pkg).treatment(treatment)
                .date(LocalDate.now()).time(LocalTime.of(10, 0)).status(status).build();
    }

    @Test
    void findAll_returnsPage() {
        when(scheduleRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(schedule)));
        var result = scheduleService.findAll(Pageable.unpaged());
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).clientName()).isEqualTo("Alice");
    }

    @Test
    void create_validRequest_savesSchedule() {
        ScheduleRequest req = new ScheduleRequest(1L, 1L, 1L, LocalDate.now(), LocalTime.of(11, 0), 1L);
        Schedule saved = Schedule.builder().id(2L).client(client).pkg(pkg).treatment(treatment)
                .date(LocalDate.now()).time(LocalTime.of(11, 0)).status(status).build();

        when(scheduleRepository.existsByClientIdAndDateAndTime(1L, LocalDate.now(), LocalTime.of(11, 0))).thenReturn(false);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(packageRepository.findById(1L)).thenReturn(Optional.of(pkg));
        when(treatmentRepository.findById(1L)).thenReturn(Optional.of(treatment));
        when(statusRepository.findById(1L)).thenReturn(Optional.of(status));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(saved);

        ScheduleResponse response = scheduleService.create(req);

        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.clientName()).isEqualTo("Alice");
    }

    @Test
    void create_doubleBooking_throwsBusinessException() {
        ScheduleRequest req = new ScheduleRequest(1L, 1L, 1L, LocalDate.now(), LocalTime.of(10, 0), 1L);
        when(scheduleRepository.existsByClientIdAndDateAndTime(1L, LocalDate.now(), LocalTime.of(10, 0))).thenReturn(true);

        assertThrows(BusinessException.class, () -> scheduleService.create(req));
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void delete_unknownId_throwsResourceNotFoundException() {
        when(scheduleRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> scheduleService.delete(99L));
        verify(scheduleRepository, never()).deleteById(any());
    }
}

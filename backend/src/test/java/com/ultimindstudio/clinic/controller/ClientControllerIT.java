package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.AbstractIntegrationTest;
import com.ultimindstudio.clinic.dto.request.ClientRequest;
import com.ultimindstudio.clinic.dto.request.LoginRequest;
import com.ultimindstudio.clinic.dto.response.ClientResponse;
import com.ultimindstudio.clinic.dto.response.LoginResponse;
import com.ultimindstudio.clinic.entity.*;
import com.ultimindstudio.clinic.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientControllerIT extends AbstractIntegrationTest {

    @Autowired TestRestTemplate restTemplate;
    @Autowired ClientRepository clientRepository;
    @Autowired StaffRepository staffRepository;
    @Autowired OccupationRepository occupationRepository;
    @Autowired LoginRepository loginRepository;
    @Autowired TypeAppRepository typeAppRepository;
    @Autowired PasswordEncoder passwordEncoder;

    Staff staff;
    String adminToken;
    String doctorToken;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
        loginRepository.deleteAll();

        Occupation occ = occupationRepository.save(Occupation.builder().name("Therapist").build());
        staff = staffRepository.save(Staff.builder().name("Dr. Smith").address("Clinic")
                .phone("555-0000").workplace("Main").active(true).occupation(occ).build());

        TypeApp adminType = typeAppRepository.findAll().stream()
                .filter(t -> "ADMIN".equals(t.getAppType())).findFirst().orElseThrow();
        TypeApp doctorType = typeAppRepository.findAll().stream()
                .filter(t -> "DOCTOR".equals(t.getAppType())).findFirst().orElseThrow();

        loginRepository.save(Login.builder().username("admin").password(passwordEncoder.encode("admin123")).typeApp(adminType).build());
        loginRepository.save(Login.builder().username("doctor").password(passwordEncoder.encode("doc123")).typeApp(doctorType).build());

        adminToken = getToken("admin", "admin123");
        doctorToken = getToken("doctor", "doc123");
    }

    @Test
    void findAll_authenticated_returns200() {
        ResponseEntity<Object> response = restTemplate.exchange(
                "/api/v1/clients", HttpMethod.GET, bearerEntity(adminToken, null), Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void findAll_unauthenticated_returns401() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/api/v1/clients", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void create_asAdmin_returns201() {
        ClientRequest req = new ClientRequest("Jane Doe", "F", "456 Oak Ave",
                "555-9876", "jane@test.com", LocalDate.of(2024, 1, 1), staff.getId());

        ResponseEntity<ClientResponse> response = restTemplate.exchange(
                "/api/v1/clients", HttpMethod.POST, bearerEntity(adminToken, req), ClientResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().name()).isEqualTo("Jane Doe");
        assertThat(response.getBody().staffName()).isEqualTo("Dr. Smith");
    }

    @Test
    void create_asDoctor_returns403() {
        ClientRequest req = new ClientRequest("Bob", "M", "Addr", "555", "b@b.com",
                LocalDate.now(), staff.getId());

        ResponseEntity<Object> response = restTemplate.exchange(
                "/api/v1/clients", HttpMethod.POST, bearerEntity(doctorToken, req), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void findById_notFound_returns404() {
        ResponseEntity<Object> response = restTemplate.exchange(
                "/api/v1/clients/99999", HttpMethod.GET, bearerEntity(adminToken, null), Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void delete_asAdmin_returns204() {
        ClientRequest req = new ClientRequest("To Delete", "M", "Addr", "555",
                "del@test.com", LocalDate.now(), staff.getId());
        ResponseEntity<ClientResponse> created = restTemplate.exchange(
                "/api/v1/clients", HttpMethod.POST, bearerEntity(adminToken, req), ClientResponse.class);
        Long id = created.getBody().id();

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/clients/" + id, HttpMethod.DELETE, bearerEntity(adminToken, null), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private String getToken(String username, String password) {
        LoginResponse resp = restTemplate.postForObject("/api/v1/auth/login",
                new LoginRequest(username, password), LoginResponse.class);
        assertThat(resp).isNotNull();
        return resp.token();
    }

    private <T> HttpEntity<T> bearerEntity(String token, T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        if (body != null) headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}

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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientControllerIT extends AbstractIntegrationTest {

    @LocalServerPort int port;

    @Autowired ClientRepository clientRepository;
    @Autowired StaffRepository staffRepository;
    @Autowired OccupationRepository occupationRepository;
    @Autowired LoginRepository loginRepository;
    @Autowired TypeAppRepository typeAppRepository;
    @Autowired PasswordEncoder passwordEncoder;

    WebTestClient client;
    Staff staff;
    String adminToken;
    String doctorToken;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        clientRepository.deleteAll();
        loginRepository.deleteAll();

        Occupation occ = occupationRepository.save(Occupation.builder().name("Therapist").build());
        staff = staffRepository.save(Staff.builder().name("Dr. Smith").address("Clinic")
                .phone("555-0000").workplace("Main").active(true).occupation(occ).build());

        TypeApp adminType = typeAppRepository.findAll().stream()
                .filter(t -> "ADMIN".equals(t.getAppType())).findFirst().orElseThrow();
        TypeApp doctorType = typeAppRepository.findAll().stream()
                .filter(t -> "DOCTOR".equals(t.getAppType())).findFirst().orElseThrow();

        loginRepository.save(Login.builder().username("admin")
                .password(passwordEncoder.encode("admin123")).typeApp(adminType).build());
        loginRepository.save(Login.builder().username("doctor")
                .password(passwordEncoder.encode("doc123")).typeApp(doctorType).build());

        adminToken = getToken("admin", "admin123");
        doctorToken = getToken("doctor", "doc123");
    }

    @Test
    void findAll_authenticated_returns200() {
        client.get().uri("/api/v1/clients")
                .header("Authorization", "Bearer " + adminToken)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void findAll_unauthenticated_returns401() {
        client.get().uri("/api/v1/clients")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void create_asAdmin_returns201() {
        ClientRequest req = new ClientRequest("Jane Doe", "F", "456 Oak Ave",
                "555-9876", "jane@test.com", LocalDate.of(2024, 1, 1), staff.getId());

        client.post().uri("/api/v1/clients")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClientResponse.class)
                .value(resp -> {
                    assertThat(resp.name()).isEqualTo("Jane Doe");
                    assertThat(resp.staffName()).isEqualTo("Dr. Smith");
                });
    }

    @Test
    void create_asDoctor_returns403() {
        ClientRequest req = new ClientRequest("Bob", "M", "Addr", "555", "b@b.com",
                LocalDate.now(), staff.getId());

        client.post().uri("/api/v1/clients")
                .header("Authorization", "Bearer " + doctorToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void findById_notFound_returns404() {
        client.get().uri("/api/v1/clients/99999")
                .header("Authorization", "Bearer " + adminToken)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void delete_asAdmin_returns204() {
        ClientRequest req = new ClientRequest("To Delete", "M", "Addr", "555",
                "del@test.com", LocalDate.now(), staff.getId());

        Long id = Objects.requireNonNull(
                client.post().uri("/api/v1/clients")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(req)
                        .exchange()
                        .expectStatus().isCreated()
                        .expectBody(ClientResponse.class)
                        .returnResult()
                        .getResponseBody()
        ).id();

        client.delete().uri("/api/v1/clients/" + id)
                .header("Authorization", "Bearer " + adminToken)
                .exchange()
                .expectStatus().isNoContent();
    }

    private String getToken(String username, String password) {
        LoginResponse resp = client.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginRequest(username, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .returnResult()
                .getResponseBody();
        assertThat(resp).isNotNull();
        return resp.token();
    }
}

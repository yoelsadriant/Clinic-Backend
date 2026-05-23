package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.AbstractIntegrationTest;
import com.ultimindstudio.clinic.dto.LookupResponse;
import com.ultimindstudio.clinic.dto.request.LoginRequest;
import com.ultimindstudio.clinic.dto.request.RegisterUserRequest;
import com.ultimindstudio.clinic.dto.response.LoginResponse;
import com.ultimindstudio.clinic.entity.Login;
import com.ultimindstudio.clinic.entity.TypeApp;
import com.ultimindstudio.clinic.repository.LoginRepository;
import com.ultimindstudio.clinic.repository.TypeAppRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIT extends AbstractIntegrationTest {

    @LocalServerPort int port;

    @Autowired TypeAppRepository typeAppRepository;
    @Autowired LoginRepository loginRepository;
    @Autowired PasswordEncoder passwordEncoder;

    WebTestClient client;
    TypeApp adminType;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        loginRepository.deleteAll();
        adminType = typeAppRepository.findAll().stream()
                .filter(t -> "ADMIN".equals(t.getAppType()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("ADMIN type_app not seeded by Flyway"));
        loginRepository.save(Login.builder()
                .username("testadmin")
                .password(passwordEncoder.encode("admin123"))
                .typeApp(adminType)
                .build());
    }

    @Test
    void login_validCredentials_returns200WithToken() {
        client.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginRequest("testadmin", "admin123"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .value(resp -> {
                    assertThat(resp.token()).isNotBlank();
                    assertThat(resp.role()).isEqualTo("ROLE_ADMIN");
                    assertThat(resp.type()).isEqualTo("Bearer");
                });
    }

    @Test
    void login_wrongPassword_returns401() {
        client.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginRequest("testadmin", "wrongpassword"))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void login_unknownUser_returns401() {
        client.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginRequest("nobody", "anything"))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void register_asAdmin_returns201() {
        String adminToken = getToken("testadmin", "admin123");

        client.post().uri("/api/v1/auth/register")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new RegisterUserRequest("newdoctor", "doc123", adminType.getId()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(LookupResponse.class)
                .value(resp -> assertThat(resp.name()).isEqualTo("newdoctor"));
    }

    @Test
    void register_withoutAuth_returns401() {
        client.post().uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new RegisterUserRequest("hacker", "pass", 1L))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void healthCheck_isPublic() {
        client.get().uri("/actuator/health")
                .exchange()
                .expectStatus().isOk();
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

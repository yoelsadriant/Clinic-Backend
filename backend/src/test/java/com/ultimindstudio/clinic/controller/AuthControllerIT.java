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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIT extends AbstractIntegrationTest {

    @Autowired TestRestTemplate restTemplate;
    @Autowired TypeAppRepository typeAppRepository;
    @Autowired LoginRepository loginRepository;
    @Autowired PasswordEncoder passwordEncoder;

    TypeApp adminType;

    @BeforeEach
    void setUp() {
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
        LoginRequest req = new LoginRequest("testadmin", "admin123");
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/api/v1/auth/login", req, LoginResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().token()).isNotBlank();
        assertThat(response.getBody().role()).isEqualTo("ROLE_ADMIN");
        assertThat(response.getBody().type()).isEqualTo("Bearer");
    }

    @Test
    void login_wrongPassword_returns401() {
        LoginRequest req = new LoginRequest("testadmin", "wrongpassword");
        ResponseEntity<Object> response = restTemplate.postForEntity("/api/v1/auth/login", req, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void login_unknownUser_returns401() {
        LoginRequest req = new LoginRequest("nobody", "anything");
        ResponseEntity<Object> response = restTemplate.postForEntity("/api/v1/auth/login", req, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void register_asAdmin_returns201() {
        String adminToken = getToken("testadmin", "admin123");
        RegisterUserRequest req = new RegisterUserRequest("newdoctor", "doc123", adminType.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        HttpEntity<RegisterUserRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<LookupResponse> response = restTemplate.exchange(
                "/api/v1/auth/register", HttpMethod.POST, entity, LookupResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().name()).isEqualTo("newdoctor");
    }

    @Test
    void register_withoutAuth_returns401() {
        RegisterUserRequest req = new RegisterUserRequest("hacker", "pass", 1L);
        ResponseEntity<Object> response = restTemplate.postForEntity("/api/v1/auth/register", req, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void healthCheck_isPublic() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/actuator/health", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private String getToken(String username, String password) {
        LoginRequest req = new LoginRequest(username, password);
        LoginResponse resp = restTemplate.postForObject("/api/v1/auth/login", req, LoginResponse.class);
        assertThat(resp).isNotNull();
        return resp.token();
    }
}

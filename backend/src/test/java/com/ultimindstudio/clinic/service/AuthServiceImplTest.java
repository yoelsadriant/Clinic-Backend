package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.LookupResponse;
import com.ultimindstudio.clinic.dto.request.LoginRequest;
import com.ultimindstudio.clinic.dto.request.RegisterUserRequest;
import com.ultimindstudio.clinic.dto.response.LoginResponse;
import com.ultimindstudio.clinic.entity.Login;
import com.ultimindstudio.clinic.entity.TypeApp;
import com.ultimindstudio.clinic.exception.BusinessException;
import com.ultimindstudio.clinic.repository.LoginRepository;
import com.ultimindstudio.clinic.repository.TypeAppRepository;
import com.ultimindstudio.clinic.security.JwtTokenProvider;
import com.ultimindstudio.clinic.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock AuthenticationManager authenticationManager;
    @Mock JwtTokenProvider tokenProvider;
    @Mock LoginRepository loginRepository;
    @Mock TypeAppRepository typeAppRepository;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks AuthServiceImpl authService;

    TypeApp adminType;

    @BeforeEach
    void setUp() {
        adminType = TypeApp.builder().id(1L).appType("ADMIN").build();
    }

    @Test
    void login_validCredentials_returnsTokenAndRole() {
        UserDetails userDetails = mock(UserDetails.class);
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("admin");
        @SuppressWarnings("unchecked")
        Collection authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        doReturn(authorities).when(userDetails).getAuthorities();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenProvider.generateToken(userDetails)).thenReturn("jwt-token");

        LoginResponse response = authService.login(new LoginRequest("admin", "password"));

        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.username()).isEqualTo("admin");
        assertThat(response.role()).isEqualTo("ROLE_ADMIN");
        assertThat(response.type()).isEqualTo("Bearer");
    }

    @Test
    void login_badCredentials_propagatesException() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad"));
        assertThrows(BadCredentialsException.class, () -> authService.login(new LoginRequest("x", "wrong")));
    }

    @Test
    void register_newUsername_createsAndReturnsUser() {
        RegisterUserRequest req = new RegisterUserRequest("newuser", "pass123", 1L);
        Login saved = Login.builder().id(5L).username("newuser").typeApp(adminType).build();
        when(loginRepository.existsByUsername("newuser")).thenReturn(false);
        when(typeAppRepository.findById(1L)).thenReturn(Optional.of(adminType));
        when(passwordEncoder.encode("pass123")).thenReturn("hashed");
        when(loginRepository.save(any(Login.class))).thenReturn(saved);

        LookupResponse response = authService.register(req);

        assertThat(response.id()).isEqualTo(5L);
        assertThat(response.name()).isEqualTo("newuser");
        verify(passwordEncoder).encode("pass123");
    }

    @Test
    void register_existingUsername_throwsBusinessException() {
        when(loginRepository.existsByUsername("existing")).thenReturn(true);
        assertThrows(BusinessException.class,
                () -> authService.register(new RegisterUserRequest("existing", "pass", 1L)));
        verify(loginRepository, never()).save(any());
    }
}

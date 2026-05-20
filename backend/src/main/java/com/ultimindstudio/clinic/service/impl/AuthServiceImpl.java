package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.LookupResponse;
import com.ultimindstudio.clinic.dto.request.LoginRequest;
import com.ultimindstudio.clinic.dto.request.RegisterUserRequest;
import com.ultimindstudio.clinic.dto.response.LoginResponse;
import com.ultimindstudio.clinic.entity.Login;
import com.ultimindstudio.clinic.entity.TypeApp;
import com.ultimindstudio.clinic.exception.BusinessException;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.LoginRepository;
import com.ultimindstudio.clinic.repository.TypeAppRepository;
import com.ultimindstudio.clinic.security.JwtTokenProvider;
import com.ultimindstudio.clinic.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final LoginRepository loginRepository;
    private final TypeAppRepository typeAppRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = tokenProvider.generateToken(userDetails);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return new LoginResponse(token, userDetails.getUsername(), role);
    }

    @Override
    public LookupResponse register(RegisterUserRequest request) {
        if (loginRepository.existsByUsername(request.username())) {
            throw new BusinessException("Username already exists: " + request.username());
        }
        TypeApp typeApp = typeAppRepository.findById(request.typeAppId())
                .orElseThrow(() -> new ResourceNotFoundException("TypeApp", "id", request.typeAppId()));
        Login login = Login.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .typeApp(typeApp)
                .build();
        Login saved = loginRepository.save(login);
        return new LookupResponse(saved.getId(), saved.getUsername());
    }
}

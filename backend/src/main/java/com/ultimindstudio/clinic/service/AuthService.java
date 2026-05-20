package com.ultimindstudio.clinic.service;

import com.ultimindstudio.clinic.dto.request.LoginRequest;
import com.ultimindstudio.clinic.dto.request.RegisterUserRequest;
import com.ultimindstudio.clinic.dto.response.LoginResponse;
import com.ultimindstudio.clinic.dto.LookupResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LookupResponse register(RegisterUserRequest request);
}

package com.anurag.productapi.service;

import com.anurag.productapi.dto.request.LoginRequest;
import com.anurag.productapi.dto.request.SignupRequest;
import com.anurag.productapi.dto.request.TokenRefreshRequest;
import com.anurag.productapi.dto.response.JwtResponse;
import com.anurag.productapi.dto.response.TokenRefreshResponse;

public interface AuthService {

    void signup(SignupRequest request);

    JwtResponse login(LoginRequest request);

    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
}
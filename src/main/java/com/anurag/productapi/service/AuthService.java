package com.anurag.productapi.service;

import com.anurag.productapi.dto.request.LoginRequest;
import com.anurag.productapi.dto.request.SignupRequest;
import com.anurag.productapi.dto.request.TokenRefreshRequest;
import com.anurag.productapi.dto.response.JwtResponse;
import com.anurag.productapi.dto.response.TokenRefreshResponse;

/**
 * Service interface for authentication operations.
 * Handles user signup, login, and JWT token refresh.
 */
public interface AuthService {

    /**
     * Registers a new user with the provided signup information.
     *
     * @param request SignupRequest DTO containing user details such as username, email, and password
     * @throws IllegalArgumentException if the signup request is invalid or username/email already exists
     */
    void signup(SignupRequest request);

    /**
     * Authenticates a user using the provided credentials and returns a JWT token.
     *
     * @param request LoginRequest DTO containing username/email and password
     * @return JwtResponse containing the access token, refresh token, and other authentication info
     * @throws IllegalArgumentException if authentication fails due to invalid credentials
     */
    JwtResponse login(LoginRequest request);

    /**
     * Refreshes the JWT access token using a valid refresh token.
     *
     * @param request TokenRefreshRequest DTO containing the refresh token
     * @return TokenRefreshResponse containing the new access token and refresh token
     * @throws IllegalArgumentException if the refresh token is invalid or expired
     */
    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
}
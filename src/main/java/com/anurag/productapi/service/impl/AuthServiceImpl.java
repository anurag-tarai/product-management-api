package com.anurag.productapi.service.impl;

import com.anurag.productapi.dto.request.*;
import com.anurag.productapi.dto.response.*;
import com.anurag.productapi.entity.RefreshToken;
import com.anurag.productapi.entity.User;
import com.anurag.productapi.enums.Roles;
import com.anurag.productapi.security.JwtUtils;
import com.anurag.productapi.security.UserDetailsImpl;
import com.anurag.productapi.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final NotificationService notificationService;

    @Override
    public void signup(SignupRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Roles.USER)
                .build();

        userService.saveUser(user);
        notificationService.sendWelcomeEmailAsync(user.getUsername());
    }

    @Override
    @Transactional
    public JwtResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        UserDetailsImpl userDetails =
                (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateToken(userDetails.getUsername());

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(userDetails.getUser());

        return new JwtResponse(
                jwt,
                refreshToken.getToken(),
                userDetails.getUsername()
        );
    }

    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {

        RefreshToken refreshToken =
                refreshTokenService.findByToken(request.getRefreshToken());

        refreshTokenService.verifyExpiration(refreshToken);

        String token =
                jwtUtils.generateToken(refreshToken.getUser().getUsername());

        return new TokenRefreshResponse(
                token,
                refreshToken.getToken()
        );
    }
}
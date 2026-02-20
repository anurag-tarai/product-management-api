package com.anurag.productapi.service.impl;


import com.anurag.productapi.entity.RefreshToken;
import com.anurag.productapi.entity.User;
import com.anurag.productapi.repository.RefreshTokenRepository;
import com.anurag.productapi.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Value("${app.jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Override
    public RefreshToken createRefreshToken(User user) {

        repository.deleteByUser(user); // refresh token rotation

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return repository.save(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }

        return token;
    }

    @Override
    public void deleteByUser(User user) {
        repository.deleteByUser(user);
    }

    @Override
    public RefreshToken findByToken(String token) {
        return repository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Refresh token not found"));
    }
}
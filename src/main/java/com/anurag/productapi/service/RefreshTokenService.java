package com.anurag.productapi.service;


import com.anurag.productapi.entity.RefreshToken;
import com.anurag.productapi.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyExpiration(RefreshToken token);

    void deleteByUser(User user);

    RefreshToken findByToken(String token);
}

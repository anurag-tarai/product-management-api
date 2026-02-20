package com.anurag.productapi.service;

import com.anurag.productapi.entity.RefreshToken;
import com.anurag.productapi.entity.User;

/**
 * Service interface for managing refresh tokens.
 * Handles creation, validation, retrieval, and deletion of refresh tokens for users.
 */
public interface RefreshTokenService {

    /**
     * Creates a new refresh token for the specified user.
     *
     * @param user the User entity for whom the refresh token is generated
     * @return the newly created RefreshToken entity
     */
    RefreshToken createRefreshToken(User user);

    /**
     * Verifies whether a given refresh token has expired.
     *
     * @param token the RefreshToken entity to check
     * @return the same RefreshToken entity if valid
     * @throws RuntimeException or custom exception if the token has expired
     */
    RefreshToken verifyExpiration(RefreshToken token);

    /**
     * Deletes all refresh tokens associated with the given user.
     *
     * @param user the User entity whose refresh tokens should be deleted
     */
    void deleteByUser(User user);

    /**
     * Finds a refresh token entity by its token string.
     *
     * @param token the string value of the refresh token
     * @return the RefreshToken entity corresponding to the provided token
     * @throws RuntimeException or custom exception if the token does not exist
     */
    RefreshToken findByToken(String token);
}
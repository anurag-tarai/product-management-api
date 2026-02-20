package com.anurag.productapi.service;

import com.anurag.productapi.entity.User;

/**
 * Service interface for managing User entities.
 * Provides basic operations for saving and retrieving users.
 */
public interface UserService {

    /**
     * Saves a new user or updates an existing user in the database.
     *
     * @param user the User entity to save
     * @return the saved User entity with updated information (e.g., ID)
     */
    User saveUser(User user);

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to retrieve
     * @return the User entity corresponding to the given username
     * @throws jakarta.persistence.EntityNotFoundException if no user is found with the provided username
     */
    User findByUsername(String username);
}
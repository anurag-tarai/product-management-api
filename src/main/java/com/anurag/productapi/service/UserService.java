package com.anurag.productapi.service;


import com.anurag.productapi.entity.User;

public interface UserService {

    User saveUser(User user);

    User findByUsername(String username);
}
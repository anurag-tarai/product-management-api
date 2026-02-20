package com.anurag.productapi.dto.request;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String password;
}
package com.financing.app.auth;

public record RegisterRequest(
        String username,
        String password,
        String email
) {
}

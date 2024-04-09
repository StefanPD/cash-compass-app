package com.financing.app.application.auth.port.in;

public record AuthenticationRequest(
        String username,
        String password
) {
}

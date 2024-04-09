package com.financing.app.auth.application.port.in;

public record AuthenticationRequest(
        String username,
        String password
) {
}

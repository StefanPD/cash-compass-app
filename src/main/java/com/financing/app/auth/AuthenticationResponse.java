package com.financing.app.auth;

public record AuthenticationResponse(
        String jwtToken,
        String refreshToken
) {
}

package com.financing.app.application.auth.port.in;

public record AuthenticationResponse(
        String jwtToken,
        String refreshToken
) {
}

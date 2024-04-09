package com.financing.app.auth.application.port.in;

public record AuthenticationResponse(
        String jwtToken,
        String refreshToken
) {
}

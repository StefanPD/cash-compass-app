package com.financing.app.auth.application.domain.model;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class AuthUtils {
    public static Optional<String> extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AuthConstants.AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith(AuthConstants.BEARER_PREFIX)) {
            return Optional.of(authHeader.substring(AuthConstants.BEARER_PREFIX.length()));
        }
        return Optional.empty();
    }
}

package com.financing.app.auth.application.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financing.app.auth.adapter.out.persistence.Token;
import com.financing.app.auth.adapter.out.persistence.TokenType;
import com.financing.app.user.adapter.out.User;
import com.financing.app.auth.application.domain.model.AuthConstants;
import com.financing.app.auth.application.port.in.AuthenticationRequest;
import com.financing.app.auth.application.port.in.AuthenticationResponse;
import com.financing.app.auth.application.port.in.RegisterRequest;
import com.financing.app.auth.application.port.out.AuthPort;
import com.financing.app.user.application.port.out.UserPort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationUseCase {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserPort userPort;
    private final AuthPort authPort;

    public AuthenticationResponse register(@Valid RegisterRequest request) {
        if (userPort.existByUsername(request.username())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userPort.existUserByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use");
        }
        var user = new User(
                request.email(),
                request.username(),
                passwordEncoder.encode(request.password())
        );
        var savedUser = userPort.saveUser(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return new AuthenticationResponse(jwtToken, refreshToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        var user = userPort.loadUserByUsername(request.username()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return new AuthenticationResponse(
                jwtToken,
                refreshToken
        );
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = new Token(
                user,
                jwtToken,
                TokenType.BEARER,
                false,
                false
        );
        authPort.saveToken(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = authPort.loadValidUserTokens(new User(user.getUserId()));
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        authPort.saveTokens(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(AuthConstants.BEARER_PREFIX)) {
            return;
        }
        var refreshToken = authHeader.substring(AuthConstants.BEARER_PREFIX.length());
        var username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = this.userPort.loadUserByUsername(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = new AuthenticationResponse(
                        accessToken,
                        refreshToken
                );
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}

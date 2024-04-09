package com.financing.app.adapter.auth.in;

import com.financing.app.application.auth.port.in.AuthenticationRequest;
import com.financing.app.application.auth.port.in.AuthenticationResponse;
import com.financing.app.application.auth.domain.service.AuthenticationUseCase;
import com.financing.app.application.auth.port.in.RegisterRequest;
import com.financing.app.utils.ApiVersion;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Validated
@ApiVersion("api/v1/auth")
@Slf4j
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationUseCase service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
}

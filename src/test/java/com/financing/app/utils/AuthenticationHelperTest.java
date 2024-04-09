package com.financing.app.utils;

import com.financing.app.auth.adapter.out.persistence.Token;
import com.financing.app.auth.adapter.out.persistence.TokenRepository;
import com.financing.app.auth.adapter.out.persistence.TokenType;
import com.financing.app.auth.application.domain.service.AuthenticationUseCase;
import com.financing.app.auth.application.port.in.RegisterRequest;
import com.financing.app.user.adapter.out.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthenticationHelperTest {

    private AuthenticationUseCase authenticationUseCase;

    private TokenRepository tokenRepository;

    public Token registerUserTest() {
        try {
            var registerRequest = new RegisterRequest(
                    "test123",
                    "t",
                    "test@email.com"
            );
            var token = authenticationUseCase.register(registerRequest);
            return new Token(
                    null,
                    token.jwtToken(),
                    TokenType.BEARER,
                    false,
                    false
            );
        } catch (Exception e) {
            return tokenRepository.findAllValidTokenByUser(new User(1L)).getFirst();
        }
    }

}

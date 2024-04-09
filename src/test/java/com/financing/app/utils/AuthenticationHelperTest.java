package com.financing.app.utils;

import com.financing.app.adapter.auth.out.persistence.Token;
import com.financing.app.adapter.auth.out.persistence.TokenRepository;
import com.financing.app.adapter.auth.out.persistence.TokenType;
import com.financing.app.application.auth.domain.service.AuthenticationUseCase;
import com.financing.app.application.auth.port.in.RegisterRequest;
import com.financing.app.adapter.user.out.User;
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

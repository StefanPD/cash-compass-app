package com.financing.app.utils;

import com.financing.app.auth.*;
import com.financing.app.user.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthenticationHelperTest {

    private AuthenticationService authenticationService;

    private TokenRepository tokenRepository;

    public Token registerUserTest() {
        var registerRequest = new RegisterRequest(
                "test123",
                "test123",
                "test@email.com"
        );
        try {
            var token = authenticationService.register(registerRequest);
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

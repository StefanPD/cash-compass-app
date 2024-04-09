package com.financing.app.auth.application.port.out;

import com.financing.app.auth.adapter.out.persistence.Token;
import com.financing.app.user.adapter.out.User;

import java.util.List;
import java.util.Optional;

public interface AuthPort {

    List<Token> loadValidUserTokens(User user);

    Optional<Token> findByToken(String token);

    void saveToken(Token token);

    void saveTokens(List<Token> tokens);
}

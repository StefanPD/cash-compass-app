package com.financing.app.application.auth.port.out;

import com.financing.app.adapter.auth.out.persistence.Token;
import com.financing.app.adapter.user.out.User;

import java.util.List;
import java.util.Optional;

public interface AuthPort {

    List<Token> loadValidUserTokens(User user);

    Optional<Token> findByToken(String token);

    void saveToken(Token token);

    void saveTokens(List<Token> tokens);
}

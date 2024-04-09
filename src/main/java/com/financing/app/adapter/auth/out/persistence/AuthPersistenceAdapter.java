package com.financing.app.adapter.auth.out.persistence;

import com.financing.app.adapter.user.out.User;
import com.financing.app.application.auth.port.out.AuthPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthPersistenceAdapter implements AuthPort {

    private final TokenRepository tokenRepository;

    @Override
    public List<Token> loadValidUserTokens(User user) {
        return tokenRepository.findAllValidTokenByUser(user);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    @Override
    public void saveTokens(List<Token> tokens) {
        tokenRepository.saveAll(tokens);
    }
}

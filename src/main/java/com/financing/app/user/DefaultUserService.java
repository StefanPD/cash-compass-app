package com.financing.app.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public Optional<UserInfo> fetchUserByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }
}

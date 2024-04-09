package com.financing.app.user.application.domain.service;

import com.financing.app.user.application.port.in.UserInfo;
import com.financing.app.user.application.port.out.UserPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserUseCase {

    private final UserPort userPort;

    @Transactional
    public Optional<UserInfo> fetchUserByUserId(Long userId) {
        return userPort.loadUser(userId);
    }
}

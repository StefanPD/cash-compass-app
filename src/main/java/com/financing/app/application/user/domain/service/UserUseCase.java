package com.financing.app.application.user.domain.service;

import com.financing.app.application.user.port.in.UserInfo;
import com.financing.app.application.user.port.out.UserPort;
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

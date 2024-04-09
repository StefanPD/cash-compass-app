package com.financing.app.user.application.port.out;

import com.financing.app.user.adapter.out.User;
import com.financing.app.user.application.port.in.UserInfo;

import java.util.Optional;

public interface UserPort {

    Optional<UserInfo> loadUser(Long userId);

    Optional<User> loadUserByUsername(String username);

    boolean existByUsername(String username);

    boolean existUserByEmail(String email);

    User saveUser(User user);
}

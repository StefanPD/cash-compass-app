package com.financing.app.user;

import java.util.Optional;

public interface UserService {
    Optional<UserInfo> fetchUserByUserId(Long userId);
}

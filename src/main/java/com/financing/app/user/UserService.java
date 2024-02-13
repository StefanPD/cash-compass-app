package com.financing.app.user;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> fetchUserByUserId(Long userId);
}

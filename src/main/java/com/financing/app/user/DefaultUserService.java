package com.financing.app.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<UserDTO> fetchUserByUserId(Long userId) {
        return userRepository.findById(userId).map(userMapper::fromUserToUserDTO);
    }
}

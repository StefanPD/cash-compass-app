package com.financing.app.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public DefaultUserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<UserDTO> fetchUserByUserId(Long userId) {
        return userRepository.findById(userId).map(userMapper::fromUserToUserDTO);
    }
}

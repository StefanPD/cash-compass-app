package com.financing.app;

import com.financing.app.user.User;
import com.financing.app.user.UserDTO;
import com.financing.app.user.UserMapper;
import com.financing.app.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class AppApplication {

    @Autowired
    public UserRepository userRepository;
    @Autowired
    private UserMapper bidMapper;
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @PostConstruct
    public void init() {

        List<UserDTO> userList = userRepository.findAll()
                .stream()
                .map(bidMapper::fromUserToUserDTO)
                .toList();
        System.out.println("has" + userList.toString());
    }

}

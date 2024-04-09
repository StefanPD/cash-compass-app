package com.financing.app.adapter.user.in.web;

import com.financing.app.application.user.domain.service.UserUseCase;
import com.financing.app.application.user.port.in.UserInfo;
import com.financing.app.utils.ApiVersion;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@ApiVersion("api/v1/users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable("userId") @Min(1) Long userId) {
        log.info("GET request received - api/v1/users/{userId}, for userId: {}", userId);
        var user = userUseCase.fetchUserByUserId(userId).orElseThrow(() -> {
            log.info("GET request failure - api/v1/users/{userId}, for userId: {}", userId);
            return new EntityNotFoundException("User with this Id doesn't exist");
        });
        return ResponseEntity.ok(user);
    }
}

package com.financing.app.user;

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
@ApiVersion("api/v1/user")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable("userId") @Min(1) Long userId) {
        log.info("GET request received - api/v1/user/{userId}, for userId: {}", userId);
        var user = userService.fetchUserByUserId(userId).orElseThrow(() -> {
            log.info("GET request failure - api/v1/user/{userId}, for userId: {}", userId);
            return new EntityNotFoundException("User with this Id doesn't exist");
        });
        return ResponseEntity.ok(user);
    }
}

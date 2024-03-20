package com.financing.app.user;

import java.time.LocalDateTime;

public record UserInfo(
        String email,
        String username,
        String password,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

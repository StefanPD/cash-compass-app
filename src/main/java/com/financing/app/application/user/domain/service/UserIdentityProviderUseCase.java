package com.financing.app.application.user.domain.service;

import com.financing.app.adapter.user.out.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserIdentityProviderUseCase {

    public User getAuthenticatedUser() throws IllegalStateException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new IllegalStateException("No authenticated user found");
    }
}

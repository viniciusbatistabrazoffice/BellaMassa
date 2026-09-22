package com.backend.dto;

import java.time.Instant;

import com.backend.entity.User;
import com.backend.entity.UserRole;
import com.backend.entity.UserStatus;

public record UserResponse(Long id, String name, String email, String phone,
        UserRole role, UserStatus status, Instant createdAt, Instant lastAccess) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(),
                user.getRole(), user.getStatus(), user.getCreatedAt(), user.getLastAccess());
    }
}

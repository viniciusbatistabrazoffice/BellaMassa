package com.backend.security;

import com.backend.entity.UserRole;

public record AuthenticatedUser(Long id, String email, UserRole role) {
}

package com.backend.dto;

public record AuthResponse(String token, UserResponse user) {
}

package com.backend.dto;

import com.backend.entity.UserStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(
        @NotNull(message = "O status é obrigatório.") UserStatus status) {
}

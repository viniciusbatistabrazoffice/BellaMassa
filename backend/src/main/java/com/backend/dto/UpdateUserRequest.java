package com.backend.dto;

import com.backend.entity.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 120, message = "O nome deve ter até 120 caracteres.") String name,
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.")
        @Size(max = 254, message = "O e-mail deve ter até 254 caracteres.") String email,
        @Size(max = 30, message = "O telefone deve ter até 30 caracteres.") String phone,
        @Size(max = 72, message = "A senha deve ter até 72 caracteres.") String password,
        @NotNull(message = "O perfil é obrigatório.") UserRole role) {
}

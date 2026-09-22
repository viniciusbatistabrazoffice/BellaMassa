package com.backend.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Usuário " + id + " não encontrado.");
    }
}

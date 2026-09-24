package com.backend.exception;

public class InactiveUserException extends RuntimeException {
    public InactiveUserException() {
        super("Esta conta está inativa. Procure um administrador.");
    }
}

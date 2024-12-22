package com.example.jwtauth.handler.exception;

public class PasswordNotEqualsException extends RuntimeException {

    public PasswordNotEqualsException() {
    }

    public PasswordNotEqualsException(String message) {
        super(message);
    }
}

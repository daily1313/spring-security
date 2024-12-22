package com.example.jwtauth.handler.exception;

public class UsernameNotEqualsException extends RuntimeException {

    public UsernameNotEqualsException() {
    }

    public UsernameNotEqualsException(String message) {
        super(message);
    }
}

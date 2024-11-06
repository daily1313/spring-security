package com.example.jwtauth.handler.exception;

public class JwtAuthenticationException extends RuntimeException {

    public JwtAuthenticationException() {
    }

    public JwtAuthenticationException(String message) {
        super(message);
    }
}

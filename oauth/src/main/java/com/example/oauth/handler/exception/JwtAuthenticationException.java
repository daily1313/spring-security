package com.example.oauth.handler.exception;

public class JwtAuthenticationException extends RuntimeException {

    public JwtAuthenticationException() {
    }

    public JwtAuthenticationException(String message) {
        super(message);
    }
}

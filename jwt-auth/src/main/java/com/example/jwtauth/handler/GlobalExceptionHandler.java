package com.example.jwtauth.handler;

import com.example.jwtauth.handler.exception.ExpiredTokenException;
import com.example.jwtauth.handler.exception.InvalidRefreshTokenException;
import com.example.jwtauth.handler.exception.JwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({JwtAuthenticationException.class, InvalidRefreshTokenException.class, ExpiredTokenException.class})
    public ResponseEntity<ErrorResponse> handleInvalidJwtException(RuntimeException e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }
}

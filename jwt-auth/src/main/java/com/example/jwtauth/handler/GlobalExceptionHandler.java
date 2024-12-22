package com.example.jwtauth.handler;

import com.example.jwtauth.handler.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({JwtAuthenticationException.class, InvalidRefreshTokenException.class, ExpiredTokenException.class})
    public ResponseEntity<?> handleInvalidJwtException(RuntimeException e) {
        return getErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(RuntimeException e) {
        return getErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({UsernameNotEqualsException.class, PasswordNotEqualsException.class})
    public ResponseEntity<?> handleNotEqualsException(RuntimeException e) {
        return getErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<?> handleAlreadyEqualsException(RuntimeException e) {
        return getErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    private ResponseEntity<?> getErrorResponse(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus)
                .body(message);
    }
}

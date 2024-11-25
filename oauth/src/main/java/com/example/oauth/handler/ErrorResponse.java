package com.example.oauth.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(staticName = "of")
public class ErrorResponse {

    private HttpStatus status;
    private String message;
}

package com.example.jwtauth.authentication.jwt;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshToken {

    @Id
    private String refreshToken;
    private String username;
}

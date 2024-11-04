package com.example.jwtauth.controller.dto;

import com.example.jwtauth.domain.Gender;

public record SignUpResponse(
        String username,
        String password,
        String nickname,
        Gender gender,
        String phoneNumber
) {
    public static SignUpResponse of(final String username,
                                    final String password,
                                    final String nickname,
                                    final Gender gender,
                                    final String phoneNumber) {
        return new SignUpResponse(username, password, nickname, gender, phoneNumber);
    }
}

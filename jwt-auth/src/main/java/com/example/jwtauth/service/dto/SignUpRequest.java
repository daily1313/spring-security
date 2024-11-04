package com.example.jwtauth.service.dto;

import com.example.jwtauth.domain.Gender;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(@NotBlank String username,
                            @NotBlank String password,
                            @NotBlank String nickname,
                            Gender gender,
                            @NotBlank String phoneNumber) {
}

package com.example.basicauth.service.dto;

import com.example.basicauth.domain.Gender;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(@NotBlank String username,
                            @NotBlank String password,
                            @NotBlank String nickname,
                            Gender gender,
                            @NotBlank String phoneNumber) {
}

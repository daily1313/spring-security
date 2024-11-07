package com.example.jwtauth.service.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(@NotBlank String refreshToken) {
}

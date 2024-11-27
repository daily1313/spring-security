package com.example.oauth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverOAuthTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") String expiresIn
) {
}

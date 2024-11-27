package com.example.oauth.controller.dto;

import com.example.oauth.domain.enums.OAuthProviderType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NaverUserInfoResponse(
        @JsonProperty("response") Response response
) implements OAuthUserInfoResponse {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            String email,
            String nickname
    ) {}

    @Override
    public String getEmail() {
        return response.email();
    }

    @Override
    public String getNickname() {
        return response.nickname();
    }

    @Override
    public OAuthProviderType getOAuthProvider() {
        return OAuthProviderType.NAVER;
    }
}
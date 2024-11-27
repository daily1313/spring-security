package com.example.oauth.controller.dto;

import com.example.oauth.domain.enums.OAuthProviderType;

public interface OAuthUserInfoResponse {
    String getEmail();

    String getNickname();
    OAuthProviderType getOAuthProvider();
}
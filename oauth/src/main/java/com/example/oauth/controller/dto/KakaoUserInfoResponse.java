package com.example.oauth.controller.dto;

import com.example.oauth.domain.enums.OAuthProviderType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoUserInfoResponse(
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) implements OAuthUserInfoResponse {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record KakaoAccount(
            KakaoProfile profile,
            String email
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record KakaoProfile(
            String nickname
    ) {}

    @Override
    public String getEmail() {
        return kakaoAccount.email();
    }

    @Override
    public String getNickname() {
        return kakaoAccount.profile().nickname();
    }

    @Override
    public OAuthProviderType getOAuthProvider() {
        return OAuthProviderType.KAKAO;
    }
}

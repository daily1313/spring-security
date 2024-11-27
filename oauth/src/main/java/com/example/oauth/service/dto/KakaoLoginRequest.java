package com.example.oauth.service.dto;

import com.example.oauth.domain.enums.OAuthProviderType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public record KakaoLoginRequest(String authorizationCode) implements OAuthRequest {

    @Override
    public OAuthProviderType oAuthProviderType() {
        return OAuthProviderType.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> toBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        return body;
    }

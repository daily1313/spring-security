package com.example.oauth.service.dto;

import com.example.oauth.domain.enums.OAuthProviderType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public record NaverLoginRequest(String authorizationCode, String state) implements OAuthRequest {

    @Override
    public OAuthProviderType oAuthProviderType() {
        return OAuthProviderType.NAVER;
    }

    @Override
    public MultiValueMap<String, String> toBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("state", state);
        return body;
    }
}

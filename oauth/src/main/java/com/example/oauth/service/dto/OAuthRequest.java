package com.example.oauth.service.dto;

import com.example.oauth.domain.enums.OAuthProviderType;
import org.springframework.util.MultiValueMap;

public interface OAuthRequest {

    OAuthProviderType oAuthProviderType();

    MultiValueMap<String, String> toBody();
}

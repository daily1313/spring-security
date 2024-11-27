package com.example.oauth.service;

import com.example.oauth.client.KakaoApiClient;
import com.example.oauth.client.NaverApiClient;
import com.example.oauth.client.OAuthApiClient;
import com.example.oauth.domain.enums.OAuthProviderType;
import org.springframework.stereotype.Component;

@Component
public class OAuthApiClientProvider {

    private final KakaoApiClient kakaoApiClient;
    private final NaverApiClient naverApiClient;

    public OAuthApiClientProvider(final KakaoApiClient kakaoApiClient, final NaverApiClient naverApiClient) {
        this.kakaoApiClient = kakaoApiClient;
        this.naverApiClient = naverApiClient;
    }

    public OAuthApiClient getApiClient(final OAuthProviderType oAuthProviderType) {
        return switch (oAuthProviderType) {
            case KAKAO -> kakaoApiClient;
            case NAVER -> naverApiClient;
            default -> throw new IllegalArgumentException();
        };
    }
}

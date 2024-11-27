package com.example.oauth.client;

import com.example.oauth.controller.dto.NaverOAuthTokenResponse;
import com.example.oauth.controller.dto.NaverUserInfoResponse;
import com.example.oauth.controller.dto.OAuthUserInfoResponse;
import com.example.oauth.domain.enums.OAuthProviderType;
import com.example.oauth.service.dto.OAuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class NaverApiClient implements OAuthApiClient {

    @Value("${oauth.naver.url.auth}")
    private String authUrl;

    @Value("${oauth.naver.url.api}")
    private String apiUrl;

    @Value("${oauth.naver.client-id}")
    private String clientId;

    @Value("${oauth.naver.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    @Override
    public String requestAccessToken(OAuthRequest oAuthRequest) {
        String url = authUrl + "/oauth2.0/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = oAuthRequest.toBody();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        NaverOAuthTokenResponse response = restTemplate
                .postForObject(url, request, NaverOAuthTokenResponse.class);

        return response.accessToken();
    }

    @Override
    public OAuthUserInfoResponse requestOauthUserInfo(String accessToken) {
        String url = apiUrl + "/v1/nid/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        NaverUserInfoResponse response = restTemplate
                .postForObject(url, request, NaverUserInfoResponse.class);

        return response;
    }
}
package com.example.oauth.client;

import com.example.oauth.controller.dto.KakaoOAuthTokenResponse;
import com.example.oauth.controller.dto.KakaoUserInfoResponse;
import com.example.oauth.controller.dto.OAuthUserInfoResponse;
import com.example.oauth.domain.enums.OAuthProviderType;
import com.example.oauth.service.dto.OAuthRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Component
public class KakaoApiClient implements OAuthApiClient {

    @Value("${oauth.kakao.url.auth}")
    private String authUrl;

    @Value("${oauth.kakao.url.api}")
    private String apiUrl;

    @Value("${oauth.kakao.url.redirect-uri}")
    private String redirectUri;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    private final RestTemplate restTemplate;

    @Override
    public String requestAccessToken(final OAuthRequest oAuthRequest) {
        String url = authUrl + "/oauth/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = oAuthRequest.toBody();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        KakaoOAuthTokenResponse response = restTemplate
                .postForObject(url, request, KakaoOAuthTokenResponse.class);

        return response.accessToken();
    }

    @Override
    public OAuthUserInfoResponse requestOauthUserInfo(String accessToken) {
        String url = apiUrl + "/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        httpHeaders.setBearerAuth(accessToken);
//        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

        try {
            KakaoUserInfoResponse kakaoUserInfoResponse = restTemplate.postForObject(url, request, KakaoUserInfoResponse.class);
            return kakaoUserInfoResponse;
        } catch (HttpClientErrorException e) {
            System.err.println("Error response from Kakao API: " + e.getResponseBodyAsString());
            throw e;
        }
    }
}

package com.example.oauth.client;

import com.example.oauth.controller.dto.OAuthUserInfoResponse;
import com.example.oauth.service.dto.OAuthRequest;

public interface OAuthApiClient {

    String requestAccessToken(OAuthRequest oAuthRequest);

    OAuthUserInfoResponse requestOauthUserInfo(String accessToken);
}

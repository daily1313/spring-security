package com.example.oauth.controller;

import com.example.oauth.controller.dto.OAuthLoginToken;
import com.example.oauth.service.OAuthService;
import com.example.oauth.service.dto.KakaoLoginRequest;
import com.example.oauth.service.dto.NaverLoginRequest;
import com.example.oauth.service.dto.OAuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v3/auth")
@RequiredArgsConstructor
@RestController
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/kakao")
    public ResponseEntity<OAuthLoginToken> loginWithKakao(@RequestBody final KakaoLoginRequest kakaoLoginRequest) {
        OAuthLoginToken token = oAuthService.login(kakaoLoginRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(token);
    }

    @PostMapping("/naver")
    public ResponseEntity<OAuthLoginToken> loginWithNaver(@RequestBody final NaverLoginRequest naverLoginRequest) {
        OAuthLoginToken token = oAuthService.login(naverLoginRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(token);
    }
}

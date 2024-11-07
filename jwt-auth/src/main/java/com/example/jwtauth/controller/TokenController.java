package com.example.jwtauth.controller;

import com.example.jwtauth.service.TokenService;
import com.example.jwtauth.service.dto.TokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/reissue")
    public ResponseEntity<String> reissueAccessToken(@RequestBody TokenRequest tokenRequest) {
        String accessToken = tokenService.reissueAccessToken(tokenRequest.refreshToken());

        return ResponseEntity.status(HttpStatus.OK)
                .body(accessToken);
    }
}

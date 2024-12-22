package com.example.jwtauth.controller;

import com.example.jwtauth.controller.dto.TokenResponse;
import com.example.jwtauth.service.TokenService;
import com.example.jwtauth.service.dto.LogoutRequest;
import com.example.jwtauth.service.dto.TokenRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueAccessToken(@Valid @RequestBody final TokenRequest tokenRequest) {
        String accessToken = tokenService.reissueAccessToken(tokenRequest.refreshToken());

        TokenResponse tokenResponse = new TokenResponse(accessToken);

        return ResponseEntity.status(HttpStatus.OK)
                .body(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody final LogoutRequest logoutRequest) {
        tokenService.logout(logoutRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}

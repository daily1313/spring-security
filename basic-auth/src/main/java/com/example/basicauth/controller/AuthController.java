package com.example.basicauth.controller;

import com.example.basicauth.controller.dto.SignUpResponse;
import com.example.basicauth.domain.Member;
import com.example.basicauth.service.AuthService;
import com.example.basicauth.service.dto.LoginRequest;
import com.example.basicauth.service.dto.SignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody final SignUpRequest signUpRequest) {
        Member member = authService.signUp(signUpRequest);

        SignUpResponse signUpResponse = SignUpResponse.of(member.getUsername(), member.getPassword(),
                member.getNickname(), member.getGender(), member.getPhoneNumber());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(signUpResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody final LoginRequest loginRequest) {
        authService.login(loginRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}

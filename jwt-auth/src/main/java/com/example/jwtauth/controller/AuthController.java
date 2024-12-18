package com.example.jwtauth.controller;

import com.example.jwtauth.controller.dto.LoginResponse;
import com.example.jwtauth.controller.dto.SignUpResponse;
import com.example.jwtauth.domain.Member;
import com.example.jwtauth.service.AuthService;
import com.example.jwtauth.service.dto.LoginRequest;
import com.example.jwtauth.service.dto.SignUpRequest;
import jakarta.validation.Valid;
import java.util.List;
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
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody final LoginRequest loginRequest) {
        List<String> loginToken = authService.login(loginRequest);
        LoginResponse loginResponse = new LoginResponse(loginToken.get(0), loginToken.get(1));
        return ResponseEntity.status(HttpStatus.OK)
                .body(loginResponse);
    }
}

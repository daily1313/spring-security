package com.example.basicauth.service;

import com.example.basicauth.domain.Member;
import com.example.basicauth.repository.MemberRepository;
import com.example.basicauth.service.dto.LoginRequest;
import com.example.basicauth.service.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;

    @Transactional
    public Member signUp(final SignUpRequest signUpRequest) {
        Member member = Member.builder()
                .username(signUpRequest.username())
                .password(passwordEncoder.encode(signUpRequest.password()))
                .nickname(signUpRequest.nickname())
                .gender(signUpRequest.gender())
                .phoneNumber(signUpRequest.phoneNumber())
                .build();

        return memberRepository.save(member);
    }

    @Transactional
    public void login(final LoginRequest loginRequest) {
        Member member = findMember(loginRequest);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());

        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Member findMember(LoginRequest loginRequest) {
        return memberRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new UsernameNotFoundException(loginRequest.username()));
    }
}

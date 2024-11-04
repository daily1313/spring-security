package com.example.jwtauth.service;

import com.example.jwtauth.authentication.jwt.JwtProvider;
import com.example.jwtauth.domain.Member;
import com.example.jwtauth.repository.MemberRepository;
import com.example.jwtauth.service.dto.LoginRequest;
import com.example.jwtauth.service.dto.SignUpRequest;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public Member signUp(final SignUpRequest signUpRequest) {
        Member member = Member.builder()
                .username(signUpRequest.username())
                .password(passwordEncoder.encode(signUpRequest.password()))
                .gender(signUpRequest.gender())
                .nickname(signUpRequest.nickname())
                .phoneNumber(signUpRequest.phoneNumber())
                .build();

        return memberRepository.save(member);
    }

    @Transactional
    public String login(final LoginRequest loginRequest) {
        Member member = findMember(loginRequest);

        String accessToken = jwtProvider.createAccessToken(member.getUsername());
        String refreshToken = jwtProvider.createRefreshToken(member.getUsername());

        redisTemplate.opsForValue().set(
                member.getUsername(),
                refreshToken,
                jwtProvider.getRefreshTokenExpirationTime(),
                TimeUnit.MILLISECONDS
        );

        return accessToken;
    }

    private Member findMember(final LoginRequest loginRequest) {
        return memberRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new UsernameNotFoundException(loginRequest.username()));
    }
}

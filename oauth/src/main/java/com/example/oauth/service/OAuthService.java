package com.example.oauth.service;

import com.example.oauth.client.OAuthApiClient;
import com.example.oauth.controller.dto.OAuthLoginToken;
import com.example.oauth.controller.dto.OAuthUserInfoResponse;
import com.example.oauth.domain.Member;
import com.example.oauth.jwt.JwtProvider;
import com.example.oauth.repository.MemberRepository;
import com.example.oauth.service.dto.OAuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OAuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final OAuthApiClientProvider oAuthApiClientProvider;

    @Transactional
    public OAuthLoginToken login(final OAuthRequest oAuthRequest) {
        OAuthApiClient apiClient = oAuthApiClientProvider.getApiClient(oAuthRequest.oAuthProviderType());

        String accessToken = apiClient.requestAccessToken(oAuthRequest);
        OAuthUserInfoResponse oAuthUserInfoResponse = apiClient.requestOauthUserInfo(accessToken);

        Long memberId = findMemberByEmail(oAuthUserInfoResponse);

        return jwtProvider.generateLoginToken(String.valueOf(memberId));
    }

    private Long findMemberByEmail(final OAuthUserInfoResponse oAuthUserInfoResponse) {
        return memberRepository.findByEmail(oAuthUserInfoResponse.getEmail())
                .map(Member::getId)
                .orElseGet(() -> registerMember(oAuthUserInfoResponse));
    }

    private Long registerMember(final OAuthUserInfoResponse oAuthUserInfoResponse) {
        Member member = Member.builder()
                .email(oAuthUserInfoResponse.getEmail())
                .nickname(oAuthUserInfoResponse.getNickname())
                .oAuthProviderType(oAuthUserInfoResponse.getOAuthProvider())
                .build();

        return memberRepository.save(member)
        
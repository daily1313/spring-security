package com.example.jwtauth.service;

import com.example.jwtauth.authentication.jwt.JwtProvider;
import com.example.jwtauth.handler.exception.ExpiredTokenException;
import com.example.jwtauth.handler.exception.InvalidRefreshTokenException;
import com.example.jwtauth.service.dto.LogoutRequest;
import com.example.jwtauth.service.dto.TokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public String reissueAccessToken(final String refreshToken) {
        validateRefreshToken(refreshToken);
        Authentication authentication = jwtProvider.getAuthentication(refreshToken);
        validateStoredRefreshToken(refreshToken, authentication.getName());

        return jwtProvider.createAccessToken(authentication.getName());
    }

    public void logout(final LogoutRequest logoutRequest) {
        String refreshToken = logoutRequest.refreshToken();
        String accessToken = logoutRequest.accessToken();

        validateRefreshToken(refreshToken);
        Authentication authentication = jwtProvider.getAuthentication(refreshToken);
        validateStoredRefreshToken(refreshToken, authentication.getName());

        // delete refresh token
        redisTemplate.delete(authentication.getName());

        // invalidate access token
        Long expiration = jwtProvider.getExpiration(accessToken);

        redisTemplate.opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    private void validateRefreshToken(final String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new ExpiredTokenException("Expired token");
        }
    }

    private void validateStoredRefreshToken(final String refreshToken, final String username) {
        String storedRefreshToken = getStoredRefreshToken(username);
        if (storedRefreshToken == null || !refreshToken.equals(storedRefreshToken)) {
            throw new InvalidRefreshTokenException("Invalid token");
        }
    }

    private String getStoredRefreshToken(final String username) {
        return redisTemplate.opsForValue().get(username);
    }
}

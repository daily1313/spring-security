package com.example.oauth.jwt;

import com.example.oauth.controller.dto.OAuthLoginToken;
import com.example.oauth.handler.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Getter
@Component
public class JwtProvider {

    private final String BEARER_PREFIX = "Bearer ";

    private SecretKey secretKey;
    private long accessTokenExpirationTime;
    private long refreshTokenExpirationTime;

    public JwtProvider(@Value("${spring.jwt.secret}") final String secretKey,
                       @Value("${spring.jwt.access-token-expiration-time}") final long accessTokenExpirationTime,
                       @Value("${spring.jwt.refresh-token-expiration-time}") final long refreshTokenExpirationTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public OAuthLoginToken generateLoginToken(final String memberId) {
        String accessToken = createAccessToken(memberId);
        String refreshToken = createRefreshToken(memberId);

        return new OAuthLoginToken(accessToken, refreshToken);
    }

    private String createAccessToken(final String memberId) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + accessTokenExpirationTime);

        return Jwts.builder()
                .setSubject(memberId)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken(final String memberId) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + refreshTokenExpirationTime);

        return Jwts.builder()
                .setSubject(memberId)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String resolveToken(final HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }

    private Claims extractClaims(final String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (UnsupportedJwtException e) {
            throw new JwtAuthenticationException("Unsupported JWT token");
        } catch (MalformedJwtException e) {
            throw new JwtAuthenticationException("Malformed JWT token");
        } catch (SignatureException e) {
            throw new JwtAuthenticationException("Invalid JWT signature");
        } catch (JwtException e) {
            throw new JwtAuthenticationException("Invalid JWT token");
        } catch (Exception e) {
            log.error("error message: {}", e.getMessage());
            throw new JwtAuthenticationException();
        }
    }
}

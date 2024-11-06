package com.example.jwtauth.authentication.jwt;

import com.example.jwtauth.handler.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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

    public Authentication getAuthentication(final String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities = Collections.emptyList();

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public String createAccessToken(final String username) {
        Claims claims = Jwts.claims().setSubject(username);
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + accessTokenExpirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.ES512)
                .compact();
    }

    public String createRefreshToken(final String username) {
        Claims claims = Jwts.claims().setSubject(username);
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + refreshTokenExpirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.ES512)
                .compact();
    }

    public String resolveToken(final HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
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

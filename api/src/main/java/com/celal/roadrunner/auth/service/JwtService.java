package com.celal.roadrunner.auth.service;

import com.celal.roadrunner.user.entity.AppUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final Duration ACCESS_TOKEN_EXPIRATION = Duration.ofMinutes(1);
    private static final Duration REFRESH_TOKEN_EXPIRATION = Duration.ofMinutes(5);

    private final JwtEncoder jwtEncoder;
    private final SecureRandom secureRandom = new SecureRandom();
    private final StringRedisTemplate  redisTemp;

    public AccessToken generateAccessToken(AppUserEntity user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(ACCESS_TOKEN_EXPIRATION);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("roadrunner-api")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .build();

        String token = jwtEncoder.encode(
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS256).build(),
                        claims
                )
        ).getTokenValue();

        return new AccessToken(token, expiresAt);
    }

    public record AccessToken(String value, Instant expiresAt) {
    }

    public RefreshToken generateRefreshToken(AppUserEntity user) {
        Instant now = Instant.now();

        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        String key = refreshTokenKey(token);
        redisTemp.opsForValue().set(
                key,
                user.getId().toString(),
                REFRESH_TOKEN_EXPIRATION
        );

        return new RefreshToken(
                token,
                now.plus(REFRESH_TOKEN_EXPIRATION)
        );
    }

    public record RefreshToken(String value, Instant expiresAt) {
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not hash refresh token", ex);
        }
    }

    public String refreshTokenKey(String token) {
        return "auth:refresh:" + hashToken(token);
    }
}

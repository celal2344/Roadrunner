package com.celal.roadrunner.auth.service;

import com.celal.roadrunner.auth.dto.LoginResponseDTO;
import com.celal.roadrunner.user.dto.AppUserDTO;
import com.celal.roadrunner.user.entity.AppUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final long ACCESS_TOKEN_EXPIRATION = 30 * 60 * 1000; // 30 min
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 days
    private final JwtEncoder jwtEncoder;

    public LoginResponseDTO generateToken(AppUserEntity user) {
        Instant expiresAt = Instant.now().plus(Duration.ofMillis(ACCESS_TOKEN_EXPIRATION));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("roadrunner-api")
                .issuedAt(Instant.now())
                .expiresAt(expiresAt)
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .build();

        return new LoginResponseDTO(
                jwtEncoder.encode(
                        JwtEncoderParameters.from(
                                JwsHeader.with(MacAlgorithm.HS256).build(),
                                claims
                        )
                ).getTokenValue(),
                "Bearer",
                expiresAt,
                AppUserDTO.fromEntity(user)
        );
    }
}

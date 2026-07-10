package com.celal.roadrunner.auth.dto;

import com.celal.roadrunner.user.dto.AppUserDTO;

import java.time.Instant;

public record LoginResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType,
        Instant accessTokenExpiresAt,
        Instant refreshTokenExpiresAt,
        AppUserDTO user
) {
}

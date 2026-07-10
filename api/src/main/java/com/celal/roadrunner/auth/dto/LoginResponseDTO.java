package com.celal.roadrunner.auth.dto;

import com.celal.roadrunner.user.dto.AppUserDTO;

import java.time.Instant;

public record LoginResponseDTO(
        String accessToken,
        String tokenType,
        Instant expiresAt,
        AppUserDTO user
) {
}

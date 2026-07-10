package com.celal.roadrunner.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDTO(
        @NotBlank(message = "{auth.refreshToken.required}")
        String refreshToken
) {
}

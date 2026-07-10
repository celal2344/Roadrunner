package com.celal.roadrunner.auth.dto;

public record LoginRequestDTO(
        String email,
        String password
) {
}

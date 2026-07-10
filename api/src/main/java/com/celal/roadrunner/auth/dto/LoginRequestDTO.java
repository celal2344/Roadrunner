package com.celal.roadrunner.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "{auth.email.required}")
        @Email(message = "{auth.email.invalid}")
        String email,

        @NotBlank(message = "{auth.password.required}")
        String password
) {
}

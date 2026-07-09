package com.celal.roadrunner.common.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ErrorResponseDTO {
    private int status;
    private String error;
    private String message;
    private Instant timestamp;
}
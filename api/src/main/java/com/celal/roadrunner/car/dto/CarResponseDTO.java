package com.celal.roadrunner.car.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarResponseDTO {
    private Long id;
    private String plate;
    private String brand;
    private String model;
    private String category;
    private BigDecimal dailyPrice;
    private boolean active;
}

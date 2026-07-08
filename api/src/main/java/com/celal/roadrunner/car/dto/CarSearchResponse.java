package com.celal.roadrunner.car.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarSearchResponse(
        Long id,
        String plate,
        String brand,
        String model,
        String category,
        BigDecimal dailyPrice,
        boolean active
) {
}

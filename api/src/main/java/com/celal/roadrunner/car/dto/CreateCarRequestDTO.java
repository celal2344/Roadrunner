package com.celal.roadrunner.car.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCarRequestDTO {
    @NotBlank(message = "{car.plate.required}")
    private String plate;

    @NotBlank(message = "{car.brand.required}")
    private String brand;

    @NotBlank(message = "{car.model.required}")
    private String model;

    @NotBlank(message = "{car.category.required}")
    private String category;

    @NotNull(message = "{car.dailyPrice.required}")
    @Positive(message = "{car.dailyPrice.positive}")
    private BigDecimal dailyPrice;

    private boolean active;}

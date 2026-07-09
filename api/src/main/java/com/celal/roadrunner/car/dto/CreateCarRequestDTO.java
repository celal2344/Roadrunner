package com.celal.roadrunner.car.dto;

import com.celal.roadrunner.car.entity.FuelType;
import com.celal.roadrunner.car.entity.TransmissionType;
import com.celal.roadrunner.car.entity.VehicleType;
import jakarta.validation.constraints.Min;
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

    @NotBlank(message = "{car.supplier.required}")
    private String supplier;

    @NotNull(message = "{car.vehicleType.required}")
    private VehicleType vehicleType;

    @NotNull(message = "{car.transmissionType.required}")
    private TransmissionType transmissionType;

    @NotNull(message = "{car.fuelType.required}")
    private FuelType fuelType;

    @Min(value = 1, message = "{car.seatCount.min}")
    private int seatCount;

    @NotNull(message = "{car.dailyPrice.required}")
    @Positive(message = "{car.dailyPrice.positive}")
    private BigDecimal dailyPrice;

    private boolean unlimitedMileage;
    private boolean flexibleCancellation;
    private boolean carlaCashEligible;
    private boolean collisionDamageWaiverIncluded;
    private boolean taxesAndFeesIncluded;
    private boolean active;
}

package com.celal.roadrunner.car.dto;

import java.math.BigDecimal;

import com.celal.roadrunner.car.entity.FuelType;
import com.celal.roadrunner.car.entity.TransmissionType;
import com.celal.roadrunner.car.entity.VehicleType;
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
    private String supplier;
    private VehicleType vehicleType;
    private TransmissionType transmissionType;
    private FuelType fuelType;
    private int seatCount;
    private BigDecimal dailyPrice;
    private boolean unlimitedMileage;
    private boolean flexibleCancellation;
    private boolean carlaCashEligible;
    private boolean collisionDamageWaiverIncluded;
    private boolean taxesAndFeesIncluded;
    private boolean active;
}

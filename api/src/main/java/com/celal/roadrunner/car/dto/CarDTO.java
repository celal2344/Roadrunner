package com.celal.roadrunner.car.dto;

import java.math.BigDecimal;

import com.celal.roadrunner.car.entity.CarEntity;
import com.celal.roadrunner.car.entity.FuelType;
import com.celal.roadrunner.car.entity.TransmissionType;
import com.celal.roadrunner.car.entity.VehicleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarDTO {
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

    public static CarDTO fromEntity(CarEntity car) {
        return CarDTO.builder()
                .id(car.getId())
                .plate(car.getPlate())
                .brand(car.getBrand())
                .model(car.getModel())
                .category(car.getCategory())
                .supplier(car.getSupplier())
                .vehicleType(car.getVehicleType())
                .transmissionType(car.getTransmissionType())
                .fuelType(car.getFuelType())
                .seatCount(car.getSeatCount())
                .dailyPrice(car.getDailyPrice())
                .unlimitedMileage(car.isUnlimitedMileage())
                .flexibleCancellation(car.isFlexibleCancellation())
                .carlaCashEligible(car.isCarlaCashEligible())
                .collisionDamageWaiverIncluded(car.isCollisionDamageWaiverIncluded())
                .taxesAndFeesIncluded(car.isTaxesAndFeesIncluded())
                .active(car.isActive())
                .build();
    }

}

package com.celal.roadrunner.car.entity;

import java.math.BigDecimal;

import com.celal.roadrunner.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cars")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String plate;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String supplier;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission_type", nullable = false)
    private TransmissionType transmissionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false)
    private FuelType fuelType;

    @Column(name = "seat_count", nullable = false)
    private int seatCount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyPrice;

    @Column(name = "unlimited_mileage", nullable = false)
    private boolean unlimitedMileage;

    @Column(name = "flexible_cancellation", nullable = false)
    private boolean flexibleCancellation;

    @Column(name = "carla_cash_eligible", nullable = false)
    private boolean carlaCashEligible;

    @Column(name = "collision_damage_waiver_included", nullable = false)
    private boolean collisionDamageWaiverIncluded;

    @Column(name = "taxes_and_fees_included", nullable = false)
    private boolean taxesAndFeesIncluded;

    @Column(nullable = false)
    private boolean active;
}

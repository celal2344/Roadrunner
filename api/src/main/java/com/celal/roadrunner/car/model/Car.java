package com.celal.roadrunner.car.model;

import java.math.BigDecimal;

import com.celal.roadrunner.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Car extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String plate;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private BigDecimal dailyPrice;

    @Column(nullable = false)
    private boolean active;
}

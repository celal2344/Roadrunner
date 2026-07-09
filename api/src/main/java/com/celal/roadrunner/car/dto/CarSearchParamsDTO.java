package com.celal.roadrunner.car.dto;

import java.math.BigDecimal;
import java.util.Set;

import com.celal.roadrunner.car.entity.FuelType;
import com.celal.roadrunner.car.entity.TransmissionType;
import com.celal.roadrunner.car.entity.VehicleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarSearchParamsDTO {
    private String q;
    private String plate;
    private String brand;
    private String model;
    private String category;

    private Set<String> suppliers;
    private Set<VehicleType> vehicleTypes;
    private Set<TransmissionType> transmissionTypes;
    private Set<FuelType> fuelTypes;

    @Positive
    private Integer minSeats;

    @Positive
    private Integer maxSeats;

    @PositiveOrZero
    private BigDecimal minDailyPrice;

    @PositiveOrZero
    private BigDecimal maxDailyPrice;

    private Boolean unlimitedMileage;
    private Boolean flexibleCancellation;
    private Boolean carlaCashEligible;
    private Boolean collisionDamageWaiverIncluded;
    private Boolean taxesAndFeesIncluded;
    private Boolean active;

    @JsonIgnore
    @AssertTrue(message = "minDailyPrice must be less than or equal to maxDailyPrice")
    public boolean isPriceRangeValid() {
        return minDailyPrice == null
                || maxDailyPrice == null
                || minDailyPrice.compareTo(maxDailyPrice) <= 0;
    }

    @JsonIgnore
    @AssertTrue(message = "minSeats must be less than or equal to maxSeats")
    public boolean isSeatRangeValid() {
        return minSeats == null || maxSeats == null || minSeats <= maxSeats;
    }
}

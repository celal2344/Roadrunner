package com.celal.roadrunner.car.repository;

import com.celal.roadrunner.car.dto.CarSearchParamsDTO;
import com.celal.roadrunner.car.entity.CarEntity;
import com.celal.roadrunner.car.entity.FuelType;
import com.celal.roadrunner.car.entity.TransmissionType;
import com.celal.roadrunner.car.entity.VehicleType;
import com.celal.roadrunner.car.specification.CarSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CarRepositoryFilterTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    void combinesMultiSelectOfferFiltersWithPageable() {
        carRepository.saveAll(List.of(
                car(
                        "34 SUV 1", "AVEC", VehicleType.SUV,
                        TransmissionType.AUTOMATIC, FuelType.DIESEL, 7, "200.00", true, true
                ),
                car(
                        "34 LUX 1", "AVEC", VehicleType.LUXURY,
                        TransmissionType.AUTOMATIC, FuelType.ELECTRIC, 5, "250.00", true, true
                ),
                car(
                        "34 CMP 1", "Hertz", VehicleType.COMPACT,
                        TransmissionType.MANUAL, FuelType.GASOLINE, 5, "100.00", false, false
                )
        ));

        CarSearchParamsDTO filters = new CarSearchParamsDTO();
        filters.setSuppliers(Set.of("avec"));
        filters.setVehicleTypes(Set.of(VehicleType.SUV, VehicleType.LUXURY));
        filters.setTransmissionTypes(Set.of(TransmissionType.AUTOMATIC));
        filters.setFuelTypes(Set.of(FuelType.DIESEL, FuelType.ELECTRIC));
        filters.setMinSeats(5);
        filters.setUnlimitedMileage(true);
        filters.setFlexibleCancellation(true);

        Page<CarEntity> result = carRepository.findAll(
                CarSpecifications.withFilters(filters),
                PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "dailyPrice"))
        );

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getContent())
                .extracting(CarEntity::getPlate)
                .containsExactly("34 SUV 1");
    }

    private CarEntity car(
            String plate,
            String supplier,
            VehicleType vehicleType,
            TransmissionType transmissionType,
            FuelType fuelType,
            int seatCount,
            String dailyPrice,
            boolean unlimitedMileage,
            boolean flexibleCancellation
    ) {
        return CarEntity.builder()
                .plate(plate)
                .brand("Test Brand")
                .model("Test Model")
                .category("Test Category")
                .supplier(supplier)
                .vehicleType(vehicleType)
                .transmissionType(transmissionType)
                .fuelType(fuelType)
                .seatCount(seatCount)
                .dailyPrice(new BigDecimal(dailyPrice))
                .unlimitedMileage(unlimitedMileage)
                .flexibleCancellation(flexibleCancellation)
                .carlaCashEligible(true)
                .collisionDamageWaiverIncluded(true)
                .taxesAndFeesIncluded(true)
                .active(true)
                .build();
    }
}

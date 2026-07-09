package com.celal.roadrunner.car.service;

import com.celal.roadrunner.car.dto.CarResponseDTO;
import com.celal.roadrunner.car.dto.CarSearchParamsDTO;
import com.celal.roadrunner.car.dto.CreateCarRequestDTO;
import com.celal.roadrunner.car.entity.CarEntity;
import com.celal.roadrunner.car.repository.CarRepository;
import com.celal.roadrunner.car.specification.CarSpecifications;
import com.celal.roadrunner.common.dto.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

public interface CarService {
    CarResponseDTO createCar(CreateCarRequestDTO createCarRequest);
    PaginatedResponse<CarResponseDTO> searchCars(CarSearchParamsDTO params, Pageable pageable);
}

@Service
@RequiredArgsConstructor
class CarServiceImpl implements CarService{
    private final CarRepository carRepo;

    @Override
    public CarResponseDTO createCar(CreateCarRequestDTO request) {
        if (carRepo.existsByPlate(request.getPlate())) {
            throw new IllegalArgumentException("Plate already exists");
        }
        CarEntity car = CarEntity.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .plate(request.getPlate())
                .category(request.getCategory())
                .supplier(request.getSupplier())
                .vehicleType(request.getVehicleType())
                .transmissionType(request.getTransmissionType())
                .fuelType(request.getFuelType())
                .seatCount(request.getSeatCount())
                .dailyPrice(request.getDailyPrice())
                .unlimitedMileage(request.isUnlimitedMileage())
                .flexibleCancellation(request.isFlexibleCancellation())
                .carlaCashEligible(request.isCarlaCashEligible())
                .collisionDamageWaiverIncluded(request.isCollisionDamageWaiverIncluded())
                .taxesAndFeesIncluded(request.isTaxesAndFeesIncluded())
                .active(request.isActive())
                .build();
        CarEntity savedCar = carRepo.save(car);
        return toResponse(savedCar);
    }

    @Override
    public PaginatedResponse<CarResponseDTO> searchCars(
            CarSearchParamsDTO params,
            Pageable pageable
    ) {
        Page<CarResponseDTO> result = carRepo
                .findAll(CarSpecifications.withFilters(params), withStableSort(pageable))
                .map(this::toResponse);

        return new PaginatedResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.hasNext(),
                result.hasPrevious()
        );
    }

    private Pageable withStableSort(Pageable pageable) {
        Sort sort = pageable.getSort();
        if (sort.getOrderFor("id") == null) {
            sort = sort.and(Sort.by(Sort.Direction.ASC, "id"));
        }
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    private CarResponseDTO toResponse(CarEntity car) {
        return CarResponseDTO.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .plate(car.getPlate())
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

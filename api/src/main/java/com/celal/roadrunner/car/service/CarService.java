package com.celal.roadrunner.car.service;

import com.celal.roadrunner.car.dto.CarResponseDTO;
import com.celal.roadrunner.car.dto.CreateCarRequestDTO;
import com.celal.roadrunner.car.model.CarModel;
import com.celal.roadrunner.car.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface CarService {
    CarResponseDTO createCar(CreateCarRequestDTO createCarRequest);
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
        CarModel car = CarModel.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .plate(request.getPlate())
                .category(request.getCategory())
                .dailyPrice(request.getDailyPrice())
                .active(request.isActive())
                .build();
        CarModel savedCar = carRepo.save(car);
        return CarResponseDTO.builder()
                .id(savedCar.getId())
                .brand(savedCar.getBrand())
                .model(savedCar.getModel())
                .plate(savedCar.getPlate())
                .category(savedCar.getCategory())
                .dailyPrice(savedCar.getDailyPrice())
                .active(savedCar.isActive())
                .build();
    }
}

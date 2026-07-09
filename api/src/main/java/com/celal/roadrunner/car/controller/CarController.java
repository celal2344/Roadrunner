package com.celal.roadrunner.car.controller;

import com.celal.roadrunner.car.dto.CarResponseDTO;
import com.celal.roadrunner.car.dto.CreateCarRequestDTO;
import com.celal.roadrunner.car.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping
    public ResponseEntity<CarResponseDTO> createCar(
            @Valid @RequestBody CreateCarRequestDTO createCarRequest
    ) {
        CarResponseDTO createdCar = carService.createCar(createCarRequest);
        return ResponseEntity.created(URI.create("/api/cars/" + createdCar.getId())).body(createdCar);
    }
}

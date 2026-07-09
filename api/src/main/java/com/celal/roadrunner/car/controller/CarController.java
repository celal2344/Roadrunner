package com.celal.roadrunner.car.controller;

import com.celal.roadrunner.car.dto.CarDTO;
import com.celal.roadrunner.car.dto.CarSearchParamsDTO;
import com.celal.roadrunner.car.dto.CreateCarRequestDTO;
import com.celal.roadrunner.car.service.CarService;
import com.celal.roadrunner.common.dto.PaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping
    public ResponseEntity<CarDTO> createCar(
            @Valid @RequestBody CreateCarRequestDTO createCarRequest
    ) {
        CarDTO createdCar = carService.createCar(createCarRequest);
        return ResponseEntity.created(URI.create("/api/cars/" + createdCar.getId())).body(createdCar);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<CarDTO>> searchCars(
            @Valid @ModelAttribute CarSearchParamsDTO params,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(carService.searchCars(params, pageable));
    }
}

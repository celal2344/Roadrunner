package com.celal.roadrunner.car.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class CarController {
    @GetMapping("/car/search")
    public ResponseEntity<Ca> getCarSearch(@PathVariable Long carId) {
        
    }
}

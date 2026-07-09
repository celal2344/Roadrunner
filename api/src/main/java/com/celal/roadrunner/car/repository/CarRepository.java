package com.celal.roadrunner.car.repository;

import com.celal.roadrunner.car.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<CarEntity, Long> {
    boolean existsByPlate(String plate);
}

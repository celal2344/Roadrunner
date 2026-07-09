package com.celal.roadrunner.car.repository;

import com.celal.roadrunner.car.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<CarModel, Long>{
    boolean existsByPlate(String plate);
}

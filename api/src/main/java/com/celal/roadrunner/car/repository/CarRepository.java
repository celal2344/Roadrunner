package com.celal.roadrunner.car.repository;

import com.celal.roadrunner.car.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CarRepository extends JpaRepository<CarEntity, Long>, JpaSpecificationExecutor<CarEntity> {
    boolean existsByPlate(String plate);
    Optional<CarEntity> findByPlate(String plate);
}

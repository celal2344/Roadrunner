package com.celal.roadrunner.car.repository;

import com.celal.roadrunner.car.entity.CarEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CarRepository extends JpaRepository<CarEntity, Long>, JpaSpecificationExecutor<CarEntity> {
    boolean existsByPlate(String plate);
    Optional<CarEntity> findByPlate(String plate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CarEntity c where c.id = :id")
    Optional<CarEntity> findByIdForUpdate(@Param("id") Long id);
}

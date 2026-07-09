package com.celal.roadrunner.booking.repository;

import com.celal.roadrunner.booking.entity.BookingEntity;
import com.celal.roadrunner.booking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.Instant;

public interface BookingRepository extends JpaRepository<BookingEntity, Long>, JpaSpecificationExecutor<BookingEntity> {
    boolean existsByCarIdAndStatusNotAndStartAtLessThanAndEndAtGreaterThan(
            Long carId,
            BookingStatus status,
            Instant endAt,
            Instant startAt
    );
}

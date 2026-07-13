package com.celal.roadrunner.booking.dto;

import com.celal.roadrunner.booking.entity.BookingEntity;
import com.celal.roadrunner.booking.entity.BookingStatus;
import com.celal.roadrunner.car.dto.CarDTO;
import com.celal.roadrunner.user.dto.AppUserDTO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class BookingDTO {
    private Long id;
    private AppUserDTO user;
    private CarDTO car;
    private Instant startAt;
    private Instant endAt;
    private BookingStatus status;
    private BigDecimal totalPrice;

    public static BookingDTO fromEntity(BookingEntity booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .user(AppUserDTO.fromEntity(booking.getUser()))
                .car(CarDTO.fromEntity(booking.getCar()))
                .startAt(booking.getStartAt())
                .endAt(booking.getEndAt())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .build();
    }
}

package com.celal.roadrunner.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CreateBookingRequestDTO {
    @NotNull(message = "{booking.car.required}")
    private Long carId;

    @NotNull(message = "{booking.startAt.required}")
    private Instant startAt;

    @NotNull(message = "{booking.endAt.required}")
    private Instant endAt;

    @NotNull(message = "{booking.totalPrice.required}")
    @Positive(message = "{booking.totalPrice.positive}")
    private BigDecimal totalPrice;
}

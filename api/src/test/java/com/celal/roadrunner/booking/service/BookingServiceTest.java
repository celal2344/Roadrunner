package com.celal.roadrunner.booking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.celal.roadrunner.booking.dto.BookingDTO;
import com.celal.roadrunner.booking.dto.CreateBookingRequestDTO;
import com.celal.roadrunner.booking.entity.BookingEntity;
import com.celal.roadrunner.booking.entity.BookingStatus;
import com.celal.roadrunner.booking.repository.BookingRepository;
import com.celal.roadrunner.car.entity.CarEntity;
import com.celal.roadrunner.car.repository.CarRepository;
import com.celal.roadrunner.common.exception.BadRequestException;
import com.celal.roadrunner.common.exception.ConflictException;
import com.celal.roadrunner.user.entity.AppUserEntity;
import com.celal.roadrunner.user.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private AppUserRepository userRepository;

    private BookingService bookingService;
    private CreateBookingRequestDTO request;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(
                bookingRepository,
                carRepository,
                userRepository
        );
        request = new CreateBookingRequestDTO();
        request.setUserId(4L);
        request.setCarId(8L);
        request.setStartAt(Instant.parse("2026-08-01T10:00:00Z"));
        request.setEndAt(Instant.parse("2026-08-04T10:00:00Z"));
        request.setTotalPrice(new BigDecimal("210.00"));
    }

    @Test
    void createsBookingWithServerControlledPendingStatus() {
        CarEntity car = mock(CarEntity.class);
        AppUserEntity user = mock(AppUserEntity.class);
        when(car.isActive()).thenReturn(true);
        when(car.getId()).thenReturn(8L);
        when(carRepository.findById(8L)).thenReturn(Optional.of(car));
        when(userRepository.findById(4L)).thenReturn(Optional.of(user));
        when(bookingRepository.save(any(BookingEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BookingDTO result = bookingService.createBooking(request);

        assertThat(result.getStatus()).isEqualTo(BookingStatus.PENDING);
        verify(bookingRepository).existsByCarIdAndStatusNotAndStartAtLessThanAndEndAtGreaterThan(
                car.getId(),
                BookingStatus.CANCELLED,
                request.getEndAt(),
                request.getStartAt()
        );
    }

    @Test
    void rejectsInvalidDateRangeBeforeDatabaseAccess() {
        request.setEndAt(request.getStartAt());

        assertThatThrownBy(() -> bookingService.createBooking(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("booking.end_after_start");
        verify(carRepository, never()).findById(any());
    }

    @Test
    void rejectsOverlappingNonCancelledBooking() {
        CarEntity car = mock(CarEntity.class);
        when(car.isActive()).thenReturn(true);
        when(car.getId()).thenReturn(8L);
        when(carRepository.findById(8L)).thenReturn(Optional.of(car));
        when(bookingRepository.existsByCarIdAndStatusNotAndStartAtLessThanAndEndAtGreaterThan(
                any(),
                any(),
                any(),
                any()
        )).thenReturn(true);

        assertThatThrownBy(() -> bookingService.createBooking(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("booking.date_overlap");
        verify(userRepository, never()).findById(any());
    }
}

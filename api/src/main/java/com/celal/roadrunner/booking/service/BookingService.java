package com.celal.roadrunner.booking.service;


import com.celal.roadrunner.booking.dto.BookingDTO;
import com.celal.roadrunner.booking.dto.CreateBookingRequestDTO;
import com.celal.roadrunner.booking.entity.BookingEntity;
import com.celal.roadrunner.booking.entity.BookingStatus;
import com.celal.roadrunner.booking.repository.BookingRepository;
import com.celal.roadrunner.car.entity.CarEntity;
import com.celal.roadrunner.car.repository.CarRepository;
import com.celal.roadrunner.common.exception.BadRequestException;
import com.celal.roadrunner.common.exception.ConflictException;
import com.celal.roadrunner.common.exception.NotFoundException;
import com.celal.roadrunner.user.entity.AppUserEntity;
import com.celal.roadrunner.user.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface BookingService {
    BookingDTO createBooking(CreateBookingRequestDTO request, Long userId);
}

@Service
@RequiredArgsConstructor
class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepo;
    private final CarRepository carRepo;
    private final AppUserRepository appUserRepo;

    @Override
    @Transactional
    public BookingDTO createBooking(CreateBookingRequestDTO request, Long userId) {
        if (!request.getEndAt().isAfter(request.getStartAt())) {
            throw new BadRequestException("booking.end_after_start");
        }

        CarEntity car = carRepo.findByIdForUpdate(request.getCarId())
                .orElseThrow(() -> new NotFoundException(
                        "car.not_found",
                        request.getCarId()
                ));

        if (!car.isActive()) {
            throw new BadRequestException("car.not_active");
        }

        boolean alreadyBooked = bookingRepo.existsByCarIdAndStatusNotAndStartAtLessThanAndEndAtGreaterThan(
                car.getId(),
                BookingStatus.CANCELLED,
                request.getEndAt(),
                request.getStartAt()
        );

        if (alreadyBooked) {
            throw new ConflictException("booking.date_overlap");
        }

        AppUserEntity appUser = appUserRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "user.not_found",
                        userId
                ));

        BookingEntity bookingEntity = BookingEntity.builder()
                .user(appUser)
                .car(car)
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .status(BookingStatus.PENDING)
                .totalPrice(request.getTotalPrice())
                .build();

        BookingEntity savedBooking = bookingRepo.save(bookingEntity);

        return BookingDTO.fromEntity(savedBooking);
    }
}

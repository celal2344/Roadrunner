package com.celal.roadrunner.common.config;

import com.celal.roadrunner.car.entity.CarEntity;
import com.celal.roadrunner.car.entity.FuelType;
import com.celal.roadrunner.car.entity.TransmissionType;
import com.celal.roadrunner.car.entity.VehicleType;
import com.celal.roadrunner.car.repository.CarRepository;
import com.celal.roadrunner.user.entity.AppUserEntity;
import com.celal.roadrunner.user.entity.Role;
import com.celal.roadrunner.user.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Profile("qa")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.qa-seeder.enabled", havingValue = "true")
public class QaDataSeeder implements CommandLineRunner {

    private static final String QA_PASSWORD = "QaPass123!";

    private final AppUserRepository userRepository;
    private final CarRepository carRepository;

    @Override
    @Transactional
    public void run(String... args) {
        seedUsers();
        seedCars();

        log.info(
                "QA seed complete: {} users, {} cars. QA password: {}",
                userRepository.count(),
                carRepository.count(),
                QA_PASSWORD
        );
    }

    private Map<String, AppUserEntity> seedUsers() {
        String passwordHash = new BCryptPasswordEncoder().encode(QA_PASSWORD);
        List<UserSeed> seeds = List.of(
                new UserSeed("QA Admin", "qa.admin@roadrunner.test", Role.ADMIN),
                new UserSeed("Ada Customer", "qa.ada@roadrunner.test", Role.CUSTOMER),
                new UserSeed("Kerem Customer", "qa.kerem@roadrunner.test", Role.CUSTOMER),
                new UserSeed("Mina Customer", "qa.mina@roadrunner.test", Role.CUSTOMER)
        );
        Map<String, AppUserEntity> users = new LinkedHashMap<>();

        for (UserSeed seed : seeds) {
            AppUserEntity user = userRepository.findByEmail(seed.email())
                    .orElseGet(() -> userRepository.save(AppUserEntity.builder()
                            .fullName(seed.fullName())
                            .email(seed.email())
                            .passwordHash(passwordHash)
                            .role(seed.role())
                            .build()));
            users.put(seed.email(), user);
        }

        return users;
    }

    private Map<String, CarEntity> seedCars() {
        List<CarSeed> seeds = List.of(
                new CarSeed("QA-001", "Toyota", "Corolla", "Economy", "AVEC", VehicleType.COMPACT,
                        TransmissionType.AUTOMATIC, FuelType.HYBRID, 5, "42.90", true, true, true, true, true, true),
                new CarSeed("QA-002", "Renault", "Clio", "Economy", "AVEC", VehicleType.COMPACT,
                        TransmissionType.MANUAL, FuelType.GASOLINE, 5, "31.50", false, true, false, false, true, true),
                new CarSeed("QA-003", "Volkswagen", "Passat", "Standard", "HERTZ", VehicleType.FULL_SIZE,
                        TransmissionType.AUTOMATIC, FuelType.DIESEL, 5, "67.00", true, false, true, true, true, true),
                new CarSeed("QA-004", "BMW", "520i", "Premium", "SIXT", VehicleType.LUXURY,
                        TransmissionType.AUTOMATIC, FuelType.GASOLINE, 5, "145.00", true, true, true, true, true, true),
                new CarSeed("QA-005", "Volvo", "XC60", "SUV", "SIXT", VehicleType.SUV,
                        TransmissionType.AUTOMATIC, FuelType.HYBRID, 5, "118.75", true, true, false, true, true, true),
                new CarSeed("QA-006", "Peugeot", "5008", "SUV", "ENTERPRISE", VehicleType.SUV,
                        TransmissionType.AUTOMATIC, FuelType.DIESEL, 7, "88.40", false, true, true, false, true, true),
                new CarSeed("QA-007", "Mercedes-Benz", "Vito", "Van", "HERTZ", VehicleType.PREMIUM_VAN,
                        TransmissionType.AUTOMATIC, FuelType.DIESEL, 9, "132.00", true, false, false, true, true, true),
                new CarSeed("QA-008", "Tesla", "Model 3", "Electric", "ENTERPRISE", VehicleType.FULL_SIZE,
                        TransmissionType.AUTOMATIC, FuelType.ELECTRIC, 5, "96.00", true, true, true, true, true, true),
                new CarSeed("QA-009", "Fiat", "Egea", "Economy", "LOCAL", VehicleType.COMPACT,
                        TransmissionType.MANUAL, FuelType.DIESEL, 5, "27.90", false, false, false, false, false, true),
                new CarSeed("QA-010", "Audi", "A6", "Premium", "AVEC", VehicleType.LUXURY,
                        TransmissionType.AUTOMATIC, FuelType.HYBRID, 5, "155.25", true, true, true, true, true, false)
        );
        Map<String, CarEntity> cars = new LinkedHashMap<>();

        for (CarSeed seed : seeds) {
            CarEntity car = carRepository.findByPlate(seed.plate())
                    .orElseGet(() -> carRepository.save(seed.toEntity()));
            cars.put(seed.plate(), car);
        }

        return cars;
    }

    private record UserSeed(String fullName, String email, Role role) {
    }

    private record CarSeed(
            String plate,
            String brand,
            String model,
            String category,
            String supplier,
            VehicleType vehicleType,
            TransmissionType transmissionType,
            FuelType fuelType,
            int seatCount,
            String dailyPrice,
            boolean unlimitedMileage,
            boolean flexibleCancellation,
            boolean carlaCashEligible,
            boolean collisionDamageWaiverIncluded,
            boolean taxesAndFeesIncluded,
            boolean active
    ) {
        private CarEntity toEntity() {
            return CarEntity.builder()
                    .plate(plate)
                    .brand(brand)
                    .model(model)
                    .category(category)
                    .supplier(supplier)
                    .vehicleType(vehicleType)
                    .transmissionType(transmissionType)
                    .fuelType(fuelType)
                    .seatCount(seatCount)
                    .dailyPrice(new BigDecimal(dailyPrice))
                    .unlimitedMileage(unlimitedMileage)
                    .flexibleCancellation(flexibleCancellation)
                    .carlaCashEligible(carlaCashEligible)
                    .collisionDamageWaiverIncluded(collisionDamageWaiverIncluded)
                    .taxesAndFeesIncluded(taxesAndFeesIncluded)
                    .active(active)
                    .build();
        }
    }
}

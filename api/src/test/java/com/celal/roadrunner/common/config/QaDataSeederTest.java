package com.celal.roadrunner.common.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.celal.roadrunner.car.entity.FuelType;
import com.celal.roadrunner.car.entity.VehicleType;
import com.celal.roadrunner.car.repository.CarRepository;
import com.celal.roadrunner.user.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:qa-seeder;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver"
})
@ActiveProfiles("qa")
class QaDataSeederTest {

    @Autowired
    private QaDataSeeder seeder;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Test
    void seedsBroadAndRepeatableQaDataset() throws Exception {
        assertThat(userRepository.count()).isEqualTo(4);
        assertThat(carRepository.count()).isEqualTo(10);
        assertThat(carRepository.findAll())
                .extracting(car -> car.getVehicleType())
                .contains(VehicleType.values());
        assertThat(carRepository.findAll())
                .extracting(car -> car.getFuelType())
                .contains(FuelType.values());

        seeder.run();

        assertThat(userRepository.count()).isEqualTo(4);
        assertThat(carRepository.count()).isEqualTo(10);
    }
}

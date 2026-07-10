package com.celal.roadrunner.car.controller;

import com.celal.roadrunner.car.dto.CarSearchParamsDTO;
import com.celal.roadrunner.car.entity.FuelType;
import com.celal.roadrunner.car.entity.TransmissionType;
import com.celal.roadrunner.car.entity.VehicleType;
import com.celal.roadrunner.car.service.CarService;
import com.celal.roadrunner.common.config.SecurityConfig;
import com.celal.roadrunner.common.dto.PaginatedResponse;
import com.celal.roadrunner.user.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
@Import(SecurityConfig.class)
class CarControllerFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @MockitoBean
    private AppUserRepository appUserRepository;

    @Test
    @WithMockUser
    void bindsFilterCollectionsAndPageableFromQueryParameters() throws Exception {
        when(carService.searchCars(any(), any())).thenReturn(
                new PaginatedResponse<>(List.of(), 1, 10, 0, 0, false, true)
        );

        mockMvc.perform(get("/api/cars")
                        .param("suppliers", "AVEC", "Hertz")
                        .param("vehicleTypes", "SUV", "LUXURY")
                        .param("transmissionTypes", "AUTOMATIC")
                        .param("fuelTypes", "DIESEL", "HYBRID")
                        .param("minSeats", "7")
                        .param("unlimitedMileage", "true")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "dailyPrice,desc"))
                .andExpect(status().isOk());

        ArgumentCaptor<CarSearchParamsDTO> filtersCaptor =
                ArgumentCaptor.forClass(CarSearchParamsDTO.class);
        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(Pageable.class);
        verify(carService).searchCars(filtersCaptor.capture(), pageableCaptor.capture());

        CarSearchParamsDTO filters = filtersCaptor.getValue();
        assertThat(filters.getSuppliers()).containsExactlyInAnyOrder("AVEC", "Hertz");
        assertThat(filters.getVehicleTypes())
                .containsExactlyInAnyOrder(VehicleType.SUV, VehicleType.LUXURY);
        assertThat(filters.getTransmissionTypes())
                .containsExactly(TransmissionType.AUTOMATIC);
        assertThat(filters.getFuelTypes())
                .containsExactlyInAnyOrder(FuelType.DIESEL, FuelType.HYBRID);
        assertThat(filters.getMinSeats()).isEqualTo(7);
        assertThat(filters.getUnlimitedMileage()).isTrue();

        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(1);
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort().getOrderFor("dailyPrice").getDirection().isDescending())
                .isTrue();
    }
}

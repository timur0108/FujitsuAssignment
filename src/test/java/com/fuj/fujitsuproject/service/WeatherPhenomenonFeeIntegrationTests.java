package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.entity.Vehicle;
import com.fuj.fujitsuproject.entity.Weather;
import com.fuj.fujitsuproject.exception.VehicleForbiddenException;
import com.fuj.fujitsuproject.repository.VehicleRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class WeatherPhenomenonFeeIntegrationTests {

    @Autowired
    private WeatherPhenomenonFeeService weatherPhenomenonFeeService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @ParameterizedTest
    @CsvSource({
            "Light snowfall, bike, 1, NONE",
            "Blowing snow, bike, 1, NONE",
            "Light sleet, bike, 1, NONE",
            "Heavy sleet storm, bike, 1, NONE",
            "Light snowfall, scooter, 1, NONE",
            "Blowing snow, scooter, 1, NONE",
            "Light sleet, scooter, 1, NONE",
            "Heavy sleet storm, scooter, 1, NONE",
            "Heavy raining, bike, 0.5, NONE",
            "Steady rainfall, bike, 0.5, NONE",
            "Light rain, bike, 0.5, NONE",
            "Heavy raining, scooter, 0.5, NONE",
            "Steady rainfall, scooter, 0.5, NONE",
            "Light rain, scooter, 0.5, NONE",
            "Glaze storm, bike, 0, VehicleForbiddenException",
            "Hailstorm, bike, 0, VehicleForbiddenException",
            "Thunderclap, bike, 0, VehicleForbiddenException",
            "Glaze storm, scooter, 0, VehicleForbiddenException",
            "Hailstorm, scooter, 0, VehicleForbiddenException",
            "Thunderclap, scooter, 0, VehicleForbiddenException",
            "thunder, car, 0, NONE"
    })
    public void testWeatherPhenomenonFeeCalculation(String weatherPhenomenon, String vehicleName,
                                                    BigDecimal expectedFee, String expectedException) {

        if ("VehicleForbiddenException".equals(expectedException)) {
            assertThrows(VehicleForbiddenException.class, () -> runTest(weatherPhenomenon, vehicleName));
        } else {
            BigDecimal feeAmount = runTest(weatherPhenomenon, vehicleName);
            assertNotNull(feeAmount);
            assertTrue(feeAmount.compareTo(expectedFee) == 0);
        }
    }

    private BigDecimal runTest(String weatherPhenomenon, String vehicleName) {

        Weather weather = new Weather();
        weather.setWeatherPhenomenon(weatherPhenomenon);

        Vehicle vehicle = vehicleRepository
                .findByNameEqualsIgnoreCase(vehicleName)
                .orElseThrow(() -> new RuntimeException());

        return weatherPhenomenonFeeService.calculateFee(vehicle, weather, Optional.ofNullable(null));
    }
}

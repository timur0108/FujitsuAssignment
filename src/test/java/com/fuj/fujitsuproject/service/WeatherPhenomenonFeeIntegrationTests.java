package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.weatherphenomenonfee.WeatherPhenomenonFeeService;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.weather.Weather;
import com.fuj.fujitsuproject.shared.exception.VehicleForbiddenException;
import com.fuj.fujitsuproject.vehicle.VehicleRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
            "thunder, car, 0, NONE",
            "Stormy sky, bike, 0, NONE",
            "Thunderstorm, bike, 0, VehicleForbiddenException",
            "Thunder, bike, 0, VehicleForbiddenException",
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

    @ParameterizedTest
    @CsvSource({
            "Foggy weather, bike, 15, NONE, 2023-12-01T12:00:00",
            "Light mist, bike, 30, NONE, 2024-02-01T12:00:00",
            "Stormy sky, bike, 0, VehicleForbiddenException, 2023-08-01T12:00:00",
            "Thunderstorm, scooter, 20, NONE, 2023-10-01T12:00:00",
    })
    public void testWeatherPhenomenonFeeCalculationWithTime(String weatherPhenomenon, String vehicleName,
                                                    BigDecimal expectedFee, String expectedException,
                                                            LocalDateTime time) {

        if ("VehicleForbiddenException".equals(expectedException)) {
            assertThrows(VehicleForbiddenException.class, () -> runTestWithTime(weatherPhenomenon, vehicleName, time));
        } else {
            BigDecimal feeAmount = runTestWithTime(weatherPhenomenon, vehicleName, time);
            assertNotNull(feeAmount);
            assertTrue(feeAmount.compareTo(expectedFee) == 0);
        }
    }

    private BigDecimal runTestWithTime(String weatherPhenomenon, String vehicleName, LocalDateTime time) {

        Weather weather = new Weather();
        weather.setWeatherPhenomenon(weatherPhenomenon);

        Vehicle vehicle = vehicleRepository
                .findByNameEqualsIgnoreCase(vehicleName)
                .orElseThrow(() -> new RuntimeException());

        return weatherPhenomenonFeeService.calculateFee(vehicle, weather, Optional.ofNullable(time));
    }
}

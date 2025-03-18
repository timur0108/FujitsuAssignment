package com.fuj.fujitsuproject.service;
import static org.junit.jupiter.api.Assertions.*;

import com.fuj.fujitsuproject.windspeedfee.WindSpeedFeeService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class WindSpeedFeeIntegrationTests {

    @Autowired
    private WindSpeedFeeService windSpeedFeeService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @ParameterizedTest
    @CsvSource({
            "10, bike, 0.5, NONE",
            "15, bike, 0.5, NONE",
            "20, bike, 0.5, NONE",
            "25, bike, 0, VehicleForbiddenException",
            "30, car, 0, NONE",
            "30, Scooter, 0, NONE",
    })
    public void testWindSpeedFeeCalculation(BigDecimal windSpeed, String vehicleName,
                                            BigDecimal expectedFee, String expectedException) {

        if ("VehicleForbiddenException".equals(expectedException)) {
            assertThrows(VehicleForbiddenException.class, () -> runTest(windSpeed, vehicleName));
        } else {
            BigDecimal feeAmount = runTest(windSpeed, vehicleName);
            assertNotNull(feeAmount);
            assertTrue(feeAmount.compareTo(expectedFee) == 0);
        }
    }

    private BigDecimal runTest(BigDecimal windSpeed, String vehicleName) {
        Weather weather = new Weather();
        weather.setWindSpeed(windSpeed);

        Vehicle vehicle = vehicleRepository
                .findByNameEqualsIgnoreCase(vehicleName)
                .orElseThrow(() -> new RuntimeException());

        return windSpeedFeeService
                .calculateFee(vehicle, weather, Optional.ofNullable(null));

    }
}

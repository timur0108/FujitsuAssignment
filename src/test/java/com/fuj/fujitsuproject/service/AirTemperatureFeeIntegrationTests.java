package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.domain.airtempfee.AirTemperatureFeeService;
import com.fuj.fujitsuproject.domain.vehicle.Vehicle;
import com.fuj.fujitsuproject.domain.weather.Weather;
import com.fuj.fujitsuproject.domain.vehicle.VehicleRepository;
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
public class AirTemperatureFeeIntegrationTests {

    @Autowired
    private AirTemperatureFeeService airTemperatureFeeService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @ParameterizedTest
    @CsvSource({
            "-15, cAr, 0",
            "-5, CAr, 0",
            "10, CAr, 0",
            "-15, scooter, 1",
            "-10, scooter, 0.5",
            "-5, scooter, 0.5",
            "0, scooter, 0.5",
            "5, scooter, 0",
            "-15, bike, 1",
            "-10, bike, 0.5",
            "-5, bike, 0.5",
            "0, bike, 0.5",
            "5, bike, 0"
    })
    public void testAirTemperatureFeeCalculation(BigDecimal temperature, String vehicleName,
                                                 BigDecimal expectedFee) {

        Weather weather = new Weather();
        weather.setAirTemperature(temperature);

        Vehicle vehicle = vehicleRepository
                .findByNameEqualsIgnoreCase(vehicleName)
                .orElseThrow(() -> new RuntimeException());

        BigDecimal feeAmount = airTemperatureFeeService.calculateFee(vehicle, weather, Optional.ofNullable(null));
        assertNotNull(feeAmount);
        assertTrue(feeAmount.compareTo(expectedFee) == 0);
    }
}

package com.fuj.fujitsuproject.airtempfee;

import com.fuj.fujitsuproject.airtempfee.AirTemperatureFeeService;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.vehicle.VehicleService;
import com.fuj.fujitsuproject.weather.Weather;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class AirTemperatureFeeIntegrationTests {

    @Autowired
    private AirTemperatureFeeService airTemperatureFeeService;

    @Autowired
    private VehicleService vehicleService;

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

        Vehicle vehicle = vehicleService.findVehicleByNameAndTime(vehicleName, Optional.ofNullable(null));

        BigDecimal feeAmount = airTemperatureFeeService.calculateFee(vehicle, weather, Optional.ofNullable(null));
        assertNotNull(feeAmount);
        assertTrue(feeAmount.compareTo(expectedFee) == 0);
    }

    @ParameterizedTest
    @CsvSource({
            "-5, scooter, 10, 2025-03-01T12:00:00",
            "-15, scooter, 15, 2025-03-01T12:00:00",
            "-15, scooter, 30, 2024-06-01T12:00:00",
            "-5, scooter, 0, 2021-03-01T12:00:00",
            "30, scooter, 0, 2021-03-01T12:00:00",
            "-5, bike, 20, 2025-03-10T12:00:00",
            "-15, bike, 25, 2025-03-10T12:00:00",
            "-15, bike, 0, 2024-03-10T12:00:00",
    })
    public void testAirTemperatureFeeCalculationWithDate(
            BigDecimal temperature, String vehicleName,
            BigDecimal expectedFee, LocalDateTime time
    ) {

        Weather weather = new Weather();
        weather.setAirTemperature(temperature);

        Vehicle vehicle = vehicleService.findVehicleByNameAndTime(vehicleName, Optional.ofNullable(time));

        BigDecimal feeAmount = airTemperatureFeeService.calculateFee(vehicle, weather, Optional.ofNullable(time));
        assertNotNull(feeAmount);
        assertTrue(feeAmount.compareTo(expectedFee) == 0);
    }
}

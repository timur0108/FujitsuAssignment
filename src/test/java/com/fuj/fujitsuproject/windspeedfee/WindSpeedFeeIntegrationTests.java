package com.fuj.fujitsuproject.windspeedfee;
import static org.junit.jupiter.api.Assertions.*;

import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.vehicle.VehicleService;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class WindSpeedFeeIntegrationTests {

    @Autowired
    private WindSpeedFeeService windSpeedFeeService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @ParameterizedTest
    @CsvSource({
            "6, bike, 0, NONE",
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

        Vehicle vehicle = vehicleService.findVehicleByNameAndTime(vehicleName, Optional.ofNullable(null));

        return windSpeedFeeService
                .calculateFee(vehicle, weather, Optional.ofNullable(null));

    }
    @ParameterizedTest
    @CsvSource({
            "6, bike, 10, NONE, 2024-04-20T12:00:00",
            "13, bike, 15, NONE, 2023-09-10T12:00:00",
            "13, bike, 30, NONE, 2023-08-01T12:00:00",
            "13, bike, 0, VehicleForbiddenException, 2023-06-01T12:00:00",
            "13, Scooter, 20, NONE, 2024-06-01T12:00:00",
    })
    public void testWindSpeedFeeCalculationWithTime(BigDecimal windSpeed, String vehicleName,
                                                    BigDecimal expectedFee, String expectedException, LocalDateTime time) {

        if ("VehicleForbiddenException".equals(expectedException)) {
            assertThrows(VehicleForbiddenException.class, () -> runTestWithTime(windSpeed, vehicleName, time));
        } else {
            BigDecimal feeAmount = runTestWithTime(windSpeed, vehicleName, time);
            System.out.println("calculated fee: " + feeAmount.toString());
            assertNotNull(feeAmount);
            assertTrue(feeAmount.compareTo(expectedFee) == 0);
        }
    }

    private BigDecimal runTestWithTime(BigDecimal windSpeed, String vehicleName, LocalDateTime time) {
        Weather weather = new Weather();
        weather.setWindSpeed(windSpeed);

        Vehicle vehicle = vehicleService
                .findVehicleByNameAndTime(vehicleName, Optional.of(time));
        return windSpeedFeeService
                .calculateFee(vehicle, weather, Optional.ofNullable(time));

    }
}

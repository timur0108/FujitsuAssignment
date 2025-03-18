package com.fuj.fujitsuproject.domain.regionalbasefee;

import static org.junit.jupiter.api.Assertions.*;

import com.fuj.fujitsuproject.city.City;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.city.CityRepository;
import com.fuj.fujitsuproject.vehicle.VehicleRepository;
import com.fuj.fujitsuproject.regionalbasefee.RegionalBaseFeeService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class RegionalBaseFeeIntegrationTests {

    @Autowired
    private RegionalBaseFeeService regionalBaseFeeService;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @ParameterizedTest
    @CsvSource({
            "bike, Tartu, 2.5",
            "Scooter, Tartu, 3",
            "cAr, Tartu, 3.5",
            "CAR, Tallinn, 4",
            "scooTER, Tallinn, 3.5",
            "BIKE, Tallinn, 3",
            "car, Pärnu, 3",
            "scooter, Pärnu, 2.5",
            "Bike, Pärnu, 2"
    })
    void testRegionalBaseFeeCalculation(String vehicleName, String cityName,
                                        BigDecimal expectedFee) {

        Vehicle vehicle = vehicleRepository.findByNameEqualsIgnoreCase(vehicleName)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        City city = cityRepository.findByNameEquals(cityName)
                .orElseThrow(() -> new RuntimeException("City not found"));

        BigDecimal feeAmount = regionalBaseFeeService.calculateFeeForVehicleAndCity(
                vehicle, city, Optional.of(LocalDateTime.now()));

        assertNotNull(feeAmount);
        assertTrue(feeAmount.compareTo(expectedFee) == 0);
    }

    @ParameterizedTest
    @CsvSource({
            "bike, Tartu, 2.3, 2025-01-01T12:00:00",
            "Scooter, Tartu, 2.8, 2025-01-01T12:00:00",
            "cAr, Tartu, 3.2, 2025-01-01T12:00:00",
            "CAR, Tallinn, 3.8, 2025-01-01T12:00:00",
            "scooTER, Tallinn, 3.3, 2025-01-01T12:00:00",
            "BIKE, Tallinn, 2.9, 2025-01-01T12:00:00",
            "car, Pärnu, 2.8, 2025-01-01T12:00:00",
            "scooter, Pärnu, 2.2, 2025-01-01T12:00:00",
            "Bike, Pärnu, 1.8, 2025-01-01T12:00:00",
            "Bike, Pärnu, 2.5, 2024-08-14T17:00:00",
    })
    void testRegionalBaseFeeCalculationWithTimeProvided(
            String vehicleName, String cityName, BigDecimal expectedFee, LocalDateTime time
    ) {
        Vehicle vehicle = vehicleRepository.findByNameEqualsIgnoreCase(vehicleName)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        City city = cityRepository.findByNameEquals(cityName)
                .orElseThrow(() -> new RuntimeException("City not found"));

        BigDecimal feeAmount = regionalBaseFeeService.calculateFeeForVehicleAndCity(
                vehicle, city, Optional.of(time));

        assertNotNull(feeAmount);
        assertTrue(feeAmount.compareTo(expectedFee) == 0);

    }
}

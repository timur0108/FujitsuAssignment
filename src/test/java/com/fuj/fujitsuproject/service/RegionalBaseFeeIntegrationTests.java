package com.fuj.fujitsuproject.service;

import static org.junit.jupiter.api.Assertions.*;
import com.fuj.fujitsuproject.entity.City;
import com.fuj.fujitsuproject.entity.Vehicle;
import com.fuj.fujitsuproject.repository.CityRepository;
import com.fuj.fujitsuproject.repository.VehicleRepository;
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
}

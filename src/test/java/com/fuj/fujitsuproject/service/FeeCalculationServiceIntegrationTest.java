package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.application.DeliveryFeeCalculationDTO;
import com.fuj.fujitsuproject.application.FeeCalculationService;
import com.fuj.fujitsuproject.shared.exception.VehicleForbiddenException;
import com.fuj.fujitsuproject.weather.Weather;
import com.fuj.fujitsuproject.weather.WeatherRepository;
import com.fuj.fujitsuproject.weather.WeatherService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class FeeCalculationServiceIntegrationTest {

    @Autowired
    private FeeCalculationService feeCalculationService;

    @MockitoBean
    private WeatherService weatherService;

    @ParameterizedTest
    @CsvSource({
            // Tallinn - Car(rbf only)
            "Tallinn,Car,5,5,clear,4",
            "Tallinn,Car,-15,5,snow,4",
            "Tallinn,Car,-30,30,snow,4",

            // Tallinn - Scooter (RBF + Temperature + Phenomenon Fees)
            "Tallinn,Scooter,5,5,clear,3.5",
            "Tallinn,Scooter,-15,5,clear,4.5", // ATEF +1
            "Tallinn,Scooter,-5,5,clear,4", // ATEF +0.5
            "Tallinn,Scooter,5,5,snow,4.5", // WPEF +1
            "Tallinn,Scooter,5,5,rain,4", // WPEF +0.5
            "Tallinn,Scooter,5,5,hail,ERROR", // Forbidden

            // Tallinn - Bike (RBF + Temperature + Wind + Phenomenon Fees)
            "Tallinn,Bike,5,5,clear,3",
            "Tallinn,Bike,-15,5,clear,4", // ATEF +1
            "Tallinn,Bike,-5,5,clear,3.5", // ATEF +0.5
            "Tallinn,Bike,5,15,clear,3.5", // WSEF +0.5
            "Tallinn,Bike,5,25,clear,ERROR", // Forbidden wind speed
            "Tallinn,Bike,5,5,snow,4", // WPEF +1
            "Tallinn,Bike,5,5,rain,3.5", // WPEF +0.5
            "Tallinn,Bike,5,5,thunder,ERROR", // Forbidden

            // Tartu - Car
            "Tartu,Car,5,5,clear,3.5",

            // Tartu - Scooter
            "Tartu,Scooter,5,5,clear,3",
            "Tartu,Scooter,-15,5,clear,4", // ATEF +1
            "Tartu,Scooter,-5,5,clear,3.5", // ATEF +0.5
            "Tartu,Scooter,5,5,snow,4", // WPEF +1
            "Tartu,Scooter,5,5,rain,3.5", // WPEF +0.5
            "Tartu,Scooter,5,5,hail,ERROR", // Forbidden

            // Tartu - Bike
            "Tartu,Bike,5,5,clear,2.5",
            "Tartu,Bike,-15,5,clear,3.5", // ATEF +1
            "Tartu,Bike,-5,5,clear,3", // ATEF +0.5
            "Tartu,Bike,5,15,clear,3", // WSEF +0.5
            "Tartu,Bike,5,25,clear,ERROR", // Forbidden wind speed
            "Tartu,Bike,5,5,snow,3.5", // WPEF +1
            "Tartu,Bike,5,5,rain,3", // WPEF +0.5
            "Tartu,Bike,5,5,thunder,ERROR", // Forbidden

            // Pärnu - Car
            "Pärnu,Car,5,5,clear,3",

            // Pärnu - Scooter
            "Pärnu,Scooter,5,5,clear,2.5",
            "Pärnu,Scooter,-15,5,clear,3.5", // ATEF +1
            "Pärnu,Scooter,-5,5,clear,3", // ATEF +0.5
            "Pärnu,Scooter,5,5,snow,3.5", // WPEF +1
            "Pärnu,Scooter,5,5,rain,3", // WPEF +0.5
            "Pärnu,Scooter,5,5,hail,ERROR", // Forbidden

            // Pärnu - Bike
            "Pärnu,Bike,5,5,clear,2",
            "Pärnu,Bike,-15,5,clear,3", // ATEF +1
            "Pärnu,Bike,-5,5,clear,2.5", // ATEF +0.5
            "Pärnu,Bike,5,15,clear,2.5", // WSEF +0.5
            "Pärnu,Bike,5,25,clear,ERROR", // Forbidden wind speed
            "Pärnu,Bike,5,5,snow,3", // WPEF +1
            "Pärnu,Bike,5,5,rain,2.5", // WPEF +0.5
            "Pärnu,Bike,5,5,thunder,ERROR" // Forbidden
    })
    public void testFeeCalculation(String city, String vehicle,
                                   BigDecimal airTemperature, BigDecimal windSpeed,
                                   String phenomenon, String expectedFee) {

        DeliveryFeeCalculationDTO deliveryFeeCalculationDTO = new DeliveryFeeCalculationDTO();
        deliveryFeeCalculationDTO.setCity(city);
        deliveryFeeCalculationDTO.setVehicle(vehicle);

        Weather testWeather = new Weather();
        testWeather.setWeatherPhenomenon(phenomenon);
        testWeather.setWindSpeed(windSpeed);
        testWeather.setAirTemperature(airTemperature);

        when(weatherService.findWeatherByCityAndTime(any(), any()))
                .thenReturn(testWeather);

        if (expectedFee.equals("ERROR")) {
            assertThrows(VehicleForbiddenException.class, () ->
                    feeCalculationService.calculateDeliveryFee(deliveryFeeCalculationDTO));
        } else {
            BigDecimal calculatedFee = feeCalculationService.calculateDeliveryFee(deliveryFeeCalculationDTO);

            assertTrue(calculatedFee.compareTo(new BigDecimal(expectedFee)) == 0);
        }


    }
}

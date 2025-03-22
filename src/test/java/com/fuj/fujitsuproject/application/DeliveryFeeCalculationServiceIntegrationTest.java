package com.fuj.fujitsuproject.application;

import com.fuj.fujitsuproject.shared.exception.VehicleForbiddenException;
import com.fuj.fujitsuproject.weather.Weather;
import com.fuj.fujitsuproject.weather.WeatherService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class DeliveryFeeCalculationServiceIntegrationTest {

    @Autowired
    private DeliveryFeeCalculationService deliveryFeeCalculationService;

    @MockitoBean
    private WeatherService weatherService;

    @ParameterizedTest
    @CsvSource({
            "Tallinn,Car,5,5,clear,4",
            "Tallinn,Car,-15,5,snow,4",
            "Tallinn,Car,-30,30,snow,4",

            "Tallinn,Scooter,5,5,clear,3.5",
            "Tallinn,Scooter,-15,5,clear,4.5",
            "Tallinn,Scooter,-5,5,clear,4",
            "Tallinn,Scooter,5,5,snow,4.5",
            "Tallinn,Scooter,5,5,rain,4",
            "Tallinn,Scooter,5,5,hail,ERROR",

            "Tallinn,Bike,5,5,clear,3",
            "Tallinn,Bike,-15,5,clear,4",
            "Tallinn,Bike,-5,5,clear,3.5",
            "Tallinn,Bike,5,15,clear,3.5",
            "Tallinn,Bike,5,25,clear,ERROR",
            "Tallinn,Bike,5,5,snow,4",
            "Tallinn,Bike,5,5,rain,3.5",
            "Tallinn,Bike,5,5,thunder,ERROR",

            "Tartu,Car,5,5,clear,3.5",

            "Tartu,Scooter,5,5,clear,3",
            "Tartu,Scooter,-15,5,clear,4",
            "Tartu,Scooter,-5,5,clear,3.5",
            "Tartu,Scooter,5,5,snow,4",
            "Tartu,Scooter,5,5,rain,3.5",
            "Tartu,Scooter,5,5,hail,ERROR",

            "Tartu,Bike,5,5,clear,2.5",
            "Tartu,Bike,-15,5,clear,3.5",
            "Tartu,Bike,-5,5,clear,3",
            "Tartu,Bike,5,15,clear,3",
            "Tartu,Bike,5,25,clear,ERROR",
            "Tartu,Bike,5,5,snow,3.5",
            "Tartu,Bike,5,5,rain,3",
            "Tartu,Bike,5,5,thunder,ERROR",

            "Pärnu,Car,5,5,clear,3",

            "Pärnu,Scooter,5,5,clear,2.5",
            "Pärnu,Scooter,-15,5,clear,3.5",
            "Pärnu,Scooter,-5,5,clear,3",
            "Pärnu,Scooter,5,5,snow,3.5",
            "Pärnu,Scooter,5,5,rain,3",
            "Pärnu,Scooter,5,5,hail,ERROR",

            "Pärnu,Bike,5,5,clear,2",
            "Pärnu,Bike,-15,5,clear,3",
            "Pärnu,Bike,-5,5,clear,2.5",
            "Pärnu,Bike,5,15,clear,2.5",
            "Pärnu,Bike,5,25,clear,ERROR",
            "Pärnu,Bike,5,5,snow,3",
            "Pärnu,Bike,5,5,rain,2.5",
            "Pärnu,Bike,5,5,thunder,ERROR"
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
                    deliveryFeeCalculationService.calculateDeliveryFee(deliveryFeeCalculationDTO));
        } else {
            BigDecimal calculatedFee = deliveryFeeCalculationService.calculateDeliveryFee(deliveryFeeCalculationDTO);

            assertTrue(calculatedFee.compareTo(new BigDecimal(expectedFee)) == 0);
        }


    }
}

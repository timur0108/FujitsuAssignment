package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.DTO.DeliveryFeeCalculationDTO;
import com.fuj.fujitsuproject.entity.City;
import com.fuj.fujitsuproject.entity.Vehicle;
import com.fuj.fujitsuproject.entity.Weather;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FeeServiceTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private FeeService feeService;

    @Test
    void testCalculateDeliveryFee() {

        DeliveryFeeCalculationDTO dto = new DeliveryFeeCalculationDTO();
        dto.setCity("Tallinn");
        dto.setVehicle("bike");

        LocalDateTime time = LocalDateTime.now();

        dto.setTime(time);

        Weather mockWeather = new Weather();
        mockWeather.setStationName("Tallinn-Harku");
        mockWeather.setWindSpeed(BigDecimal.valueOf(20));
        mockWeather.setAirTemperature(BigDecimal.valueOf(-12));

        when(weatherService.findWeatherByCityAndTime(mock(City.class), time)).thenReturn(mockWeather);

        BigDecimal result = feeService.calculateDeliveryFee(dto);

        Assertions.assertEquals(BigDecimal.TEN, result);

    }
}

package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.DTO.DeliveryFeeCalculationDTO;
import com.fuj.fujitsuproject.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeeCalculationService {

    private final List<WeatherBasedFeeService> weatherBasedFeeServices;
    private final CityService cityService;
    private final VehicleService vehicleService;
    private final RegionalBaseFeeService regionalBaseFeeService;
    private final WeatherService weatherService;

    public BigDecimal calculateDeliveryFee(DeliveryFeeCalculationDTO deliveryFeeCalculationDTO) {

        log.info("Calculating delivery fee for {}", deliveryFeeCalculationDTO);

        String cityName = deliveryFeeCalculationDTO.getCity();
        City city = cityService.findCityByName(cityName);

        String vehicleName = deliveryFeeCalculationDTO.getVehicle();
        Vehicle vehicle = vehicleService.findVehicleByName(vehicleName);

        final Optional<LocalDateTime> timeToSearch = Optional.ofNullable(deliveryFeeCalculationDTO.getTime());

        BigDecimal totalFee = BigDecimal.ZERO;

        totalFee = totalFee.add(regionalBaseFeeService
                .calculateFeeForVehicleAndCity(vehicle, city, timeToSearch));
        log.info("Regional base fee = {}", totalFee);

        Weather weather = weatherService.findWeatherByCityAndTime(city, timeToSearch);

        totalFee = weatherBasedFeeServices
                .stream()
                .map(service -> service.calculateFee(vehicle, weather, timeToSearch))
                .reduce(totalFee, BigDecimal::add);

        return totalFee;
    }
}

package com.fuj.fujitsuproject.application;

import com.fuj.fujitsuproject.city.City;
import com.fuj.fujitsuproject.regionalbasefee.RegionalBaseFeeService;
import com.fuj.fujitsuproject.shared.service.VehicleAndWeatherBasedFeeService;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.weather.Weather;
import com.fuj.fujitsuproject.city.CityService;
import com.fuj.fujitsuproject.vehicle.VehicleService;
import com.fuj.fujitsuproject.weather.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Main service that processes the request for calculation of delivery fee
 * based on vehicle and city.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryFeeCalculationService {

    private final List<VehicleAndWeatherBasedFeeService> vehicleAndWeatherBasedFeeServices;
    private final CityService cityService;
    private final VehicleService vehicleService;
    private final RegionalBaseFeeService regionalBaseFeeService;
    private final WeatherService weatherService;

    /**
     * Calculates the total delivery fee.
     * Fee is calculated considering regional base fee and all implemented vehicle and weather
     * based fee services.
     * @param deliveryFeeCalculationDTO the DTO containing the information needed to calculate the fee.
     *                                  Includes the city, vehicle and optional time.
     * @return the total calculated delivery fee based on regional base fee and
     * weather and vehicle based fees.
     */
    public BigDecimal calculateDeliveryFee(DeliveryFeeCalculationDTO deliveryFeeCalculationDTO) {

        log.info("Calculating delivery fee for {}", deliveryFeeCalculationDTO);

        final Optional<LocalDateTime> time = Optional.ofNullable(
                deliveryFeeCalculationDTO.getTime());

        String cityName = deliveryFeeCalculationDTO.getCity();
        City city = cityService.findCityByNameAndTime(cityName, time);

        String vehicleName = deliveryFeeCalculationDTO.getVehicle();
        Vehicle vehicle = vehicleService.findVehicleByName(vehicleName);


        BigDecimal totalFee = BigDecimal.ZERO;
        totalFee = totalFee.add(regionalBaseFeeService
                .calculateFeeForVehicleAndCity(vehicle, city, time));

        Weather weather = weatherService.findWeatherByCityAndTime(city, time);

        totalFee = vehicleAndWeatherBasedFeeServices
                .stream()
                .map(service -> service.calculateFee(vehicle, weather, time))
                .reduce(totalFee, BigDecimal::add);

        return totalFee;
    }
}

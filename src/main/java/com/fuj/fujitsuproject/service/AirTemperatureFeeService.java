package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.entity.AirTemperatureFee;
import com.fuj.fujitsuproject.entity.Vehicle;
import com.fuj.fujitsuproject.entity.Weather;
import com.fuj.fujitsuproject.exception.VehicleForbiddenException;
import com.fuj.fujitsuproject.repository.AirTemperatureFeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirTemperatureFeeService implements WeatherBasedFeeService{

    private final AirTemperatureFeeRepository airTemperatureFeeRepository;

    private Optional<AirTemperatureFee> findAirTemperatureFeeByVehicleAndWeatherAndTime(
            Vehicle vehicle, Weather weather, LocalDateTime time) {

        return airTemperatureFeeRepository
                .findAirTemperatureFeeByVehicleIdAndTemperatureAndTime(
                        vehicle.getId(), weather.getAirTemperature(), time);
    }

    @Override
    public BigDecimal calculateFee(Vehicle vehicle, Weather weather, LocalDateTime time) {

        return findAirTemperatureFeeByVehicleAndWeatherAndTime(
                vehicle, weather, time)
                .map(fee -> {
                    if (fee.isForbidden()) throw new VehicleForbiddenException();
                    return fee.getAmount();
                })
                .orElse(BigDecimal.ZERO);

    }
}

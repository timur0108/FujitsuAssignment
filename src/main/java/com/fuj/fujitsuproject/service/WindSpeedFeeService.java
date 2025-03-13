package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.entity.AirTemperatureFee;
import com.fuj.fujitsuproject.entity.Vehicle;
import com.fuj.fujitsuproject.entity.Weather;
import com.fuj.fujitsuproject.entity.WindSpeedFee;
import com.fuj.fujitsuproject.exception.VehicleForbiddenException;
import com.fuj.fujitsuproject.repository.WindSpeedFeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class WindSpeedFeeService implements WeatherBasedFeeService{

    private final WindSpeedFeeRepository windSpeedFeeRepository;

    private Optional<WindSpeedFee> findWindSpeedFeeByVehicleAndWeatherAndTime(
            Vehicle vehicle, Weather weather, LocalDateTime time) {

        return windSpeedFeeRepository
                .findWindSpeedFeeByVehicleIdAndWindSpeedAndTime(
                        vehicle.getId(), weather.getWindSpeed(), time);
    }

    @Override
    public BigDecimal calculateFee(Vehicle vehicle, Weather weather, LocalDateTime time) {

        return findWindSpeedFeeByVehicleAndWeatherAndTime(
                vehicle, weather, time)
                .map(fee -> {
                    if (fee.isForbidden()) throw new VehicleForbiddenException();
                    return fee.getAmount();
                })
                .orElse(BigDecimal.ZERO);

    }
}

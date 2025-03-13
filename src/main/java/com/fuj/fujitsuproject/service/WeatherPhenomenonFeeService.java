package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.entity.Vehicle;
import com.fuj.fujitsuproject.entity.Weather;
import com.fuj.fujitsuproject.entity.WeatherPhenomenonFee;
import com.fuj.fujitsuproject.entity.WindSpeedFee;
import com.fuj.fujitsuproject.exception.VehicleForbiddenException;
import com.fuj.fujitsuproject.repository.WeatherPhenomenonFeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherPhenomenonFeeService implements WeatherBasedFeeService{

    private final WeatherPhenomenonFeeRepository weatherPhenomenonFeeRepository;

    private Optional<WeatherPhenomenonFee> findWeatherPhenomenonFeeByVehicleAndWeatherAndTime(
            Vehicle vehicle, Weather weather, LocalDateTime time) {

        return weatherPhenomenonFeeRepository
                .findWeatherPhenomenonFeeByVehicleIdAndPhenomenonAndTime(
                        vehicle.getId(), weather.getWeatherPhenomenon(), time);
    }

    @Override
    public BigDecimal calculateFee(Vehicle vehicle, Weather weather, LocalDateTime time) {

        return findWeatherPhenomenonFeeByVehicleAndWeatherAndTime(
                vehicle, weather, time)
                .map(fee -> {
                    if (fee.isForbidden()) throw new VehicleForbiddenException();
                    return fee.getAmount();
                })
                .orElse(BigDecimal.ZERO);
    }
}

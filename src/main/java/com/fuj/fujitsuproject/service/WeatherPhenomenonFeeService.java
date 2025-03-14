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

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherPhenomenonFeeService implements WeatherBasedFeeService{

    private final WeatherPhenomenonFeeRepository weatherPhenomenonFeeRepository;

    private Optional<WeatherPhenomenonFee> findWpefByVehicleAndWeatherAndTime(
            Vehicle vehicle, Weather weather, LocalDateTime time) {

        return weatherPhenomenonFeeRepository
                .findWeatherPhenomenonFeeByVehicleIdAndPhenomenonAndTime(
                        vehicle.getId(), weather.getWeatherPhenomenon().toLowerCase(), time);
    }

    private Optional<WeatherPhenomenonFee> findLatestWpefByVehicleAndWeather(Vehicle vehicle, Weather weather) {
        log.info("weather phenomenon=" + weather.getWeatherPhenomenon());
        return weatherPhenomenonFeeRepository
                .findLatestWeatherPhenomenonFeeByVehicleIdAndPhenomenon(vehicle.getId(), weather.getWeatherPhenomenon().toLowerCase());
    }

    @Override
    public BigDecimal calculateFee(Vehicle vehicle, Weather weather, Optional<LocalDateTime> time) {
        Optional<WeatherPhenomenonFee> weatherPhenomenonFeeOptional;

        if (time.isPresent()) weatherPhenomenonFeeOptional = findWpefByVehicleAndWeatherAndTime(
                vehicle, weather, time.get());
        else weatherPhenomenonFeeOptional = findLatestWpefByVehicleAndWeather(vehicle, weather);

        if (weatherPhenomenonFeeOptional.isEmpty()) return BigDecimal.ZERO;

        WeatherPhenomenonFee weatherPhenomenonFee = weatherPhenomenonFeeOptional.get();
        if (weatherPhenomenonFee.isForbidden()) throw new VehicleForbiddenException();

        log.info("phenomenon fee=" + weatherPhenomenonFee.getAmount().toString());
        return weatherPhenomenonFee.getAmount();
    }
}

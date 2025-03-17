package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.domain.vehicle.Vehicle;
import com.fuj.fujitsuproject.domain.weather.Weather;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface WeatherBasedFeeService {

    BigDecimal calculateFee(Vehicle vehicle, Weather weather, Optional<LocalDateTime> time);
}

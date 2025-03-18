package com.fuj.fujitsuproject.shared.service;

import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.weather.Weather;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface WeatherBasedFeeService {

    BigDecimal calculateFee(Vehicle vehicle, Weather weather, Optional<LocalDateTime> time);
}

package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.entity.Vehicle;
import com.fuj.fujitsuproject.entity.Weather;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface WeatherBasedFeeService {

    BigDecimal calculateFee(Vehicle vehicle, Weather weather, LocalDateTime time);
}

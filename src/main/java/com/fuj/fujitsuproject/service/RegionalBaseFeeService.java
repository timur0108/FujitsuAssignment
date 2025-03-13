package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.entity.City;
import com.fuj.fujitsuproject.entity.RegionalBaseFee;
import com.fuj.fujitsuproject.entity.Vehicle;
import com.fuj.fujitsuproject.repository.RegionalBaseFeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegionalBaseFeeService {

    private final RegionalBaseFeeRepository regionalBaseFeeRepository;

    private RegionalBaseFee findRegionalBaseFeeForVehicleAndCityAndTime(Vehicle vehicle, City city, LocalDateTime time) {
        return regionalBaseFeeRepository
                .findRbfForParticularTimeByCityIdAndVehicleId(
                        city.getId(), vehicle.getId(), time).orElseThrow(() -> new EntityNotFoundException("" +
                        "Couldn't find regional base fee for vehicle=" + vehicle + ", city=" + city +
                        ", time=" + time));
    }

    public BigDecimal calculateFeeForVehicleAndCity(Vehicle vehicle, City city, LocalDateTime time) {
        return findRegionalBaseFeeForVehicleAndCityAndTime(vehicle, city, time)
                .getAmount();
    }
}

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
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegionalBaseFeeService {

    private final RegionalBaseFeeRepository regionalBaseFeeRepository;

    private RegionalBaseFee findRbfByVehicleAndCityAndTime(Vehicle vehicle, City city, LocalDateTime time) {
        return regionalBaseFeeRepository
                .findRbfForParticularTimeByCityIdAndVehicleId(city.getId(), vehicle.getId(), time)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find regional base fee for vehicle=" + vehicle + ", city=" + city +
                        ", time=" + time));
    }

    private RegionalBaseFee findLatestRbfByVehicleAndCity (Vehicle vehicle, City city) {
        return regionalBaseFeeRepository
                .findLatestActiveRbfByCityIdAndVehicleId(vehicle.getId(), city.getId())
                .orElseThrow(() -> new EntityNotFoundException("" +
                        "Couldn't find latest active regional base fee for vehicle" +
                        "=" + vehicle + ", city=" + city));
    }

    public BigDecimal calculateFeeForVehicleAndCity(Vehicle vehicle, City city, Optional<LocalDateTime> time) {

        RegionalBaseFee regionalBaseFee;

        if (time.isPresent()) regionalBaseFee = findRbfByVehicleAndCityAndTime(vehicle, city, time.get());
        else regionalBaseFee = findLatestRbfByVehicleAndCity(vehicle, city);

        log.info("regional base fee=" + regionalBaseFee.getAmount());
        return regionalBaseFee.getAmount();
    }
}

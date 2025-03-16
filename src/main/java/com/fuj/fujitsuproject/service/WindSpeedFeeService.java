package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.DTO.WindSpeedFeeCreateDTO;
import com.fuj.fujitsuproject.entity.Vehicle;
import com.fuj.fujitsuproject.entity.Weather;
import com.fuj.fujitsuproject.entity.WindSpeedFee;
import com.fuj.fujitsuproject.exception.OverlappingWindSpeedFeeException;
import com.fuj.fujitsuproject.exception.VehicleForbiddenException;
import com.fuj.fujitsuproject.repository.WindSpeedFeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class WindSpeedFeeService implements WeatherBasedFeeService{

    private final WindSpeedFeeRepository windSpeedFeeRepository;

    private final VehicleService vehicleService;

    public List<WindSpeedFee> findAllWindSpeedFees() {

        return windSpeedFeeRepository.findAll();
    }

    public WindSpeedFee createWindSpeedFee(WindSpeedFeeCreateDTO windSpeedFeeCreateDTO) {

        Vehicle vehicle = vehicleService
                .findVehicleById(windSpeedFeeCreateDTO.getVehicleId());

        BigDecimal minSpeed = windSpeedFeeCreateDTO.getMinSpeed();
        BigDecimal maxSpeed = windSpeedFeeCreateDTO.getMaxSpeed();

        List<WindSpeedFee> overlappingWindSpeedFees = windSpeedFeeRepository
                .findActiveWindSpeedFeesWithOverlappingSpeedRangeByVehicleId(
                       minSpeed, maxSpeed, vehicle.getId());

        if (!overlappingWindSpeedFees.isEmpty()) throw new OverlappingWindSpeedFeeException(overlappingWindSpeedFees);

        WindSpeedFee windSpeedFee = new WindSpeedFee();
        windSpeedFee.setMinSPeed(minSpeed);
        windSpeedFee.setMaxSPeed(maxSpeed);
        windSpeedFee.setAmount(windSpeedFeeCreateDTO.getAmount());
        windSpeedFee.setForbidden(windSpeedFee.isForbidden());
        windSpeedFee.setVehicle(vehicle);
        windSpeedFee.setActive(true);

        return windSpeedFeeRepository.save(windSpeedFee);
    }

    private Optional<WindSpeedFee> findWsefByVehicleAndWeatherAndTime(
            Vehicle vehicle, Weather weather, LocalDateTime time) {

        return windSpeedFeeRepository
                .findWindSpeedFeeByVehicleIdAndWindSpeedAndTime(
                        vehicle.getId(), weather.getWindSpeed(), time);
    }

    private Optional<WindSpeedFee> findLatestWsefByVehicleAndWeather(
            Vehicle vehicle, Weather weather) {

        return windSpeedFeeRepository
                .findLatestActiveWindSpeedFeeByVehicleIdAndSpeed(
                        vehicle.getId(), weather.getWindSpeed());

    }

    @Override
    public BigDecimal calculateFee(Vehicle vehicle, Weather weather, Optional<LocalDateTime> time) {

        Optional<WindSpeedFee> windSpeedFeeOptional;

        if (time.isPresent()) windSpeedFeeOptional = findWsefByVehicleAndWeatherAndTime(vehicle, weather, time.get());
        else windSpeedFeeOptional = findLatestWsefByVehicleAndWeather(vehicle, weather);

        if (windSpeedFeeOptional.isEmpty()) return BigDecimal.ZERO;

        WindSpeedFee fee = windSpeedFeeOptional.get();
        if (fee.isForbidden()) throw new VehicleForbiddenException();

        log.info("wind speed fee=" + fee.getAmount().toString());
        return fee.getAmount();
    }
}

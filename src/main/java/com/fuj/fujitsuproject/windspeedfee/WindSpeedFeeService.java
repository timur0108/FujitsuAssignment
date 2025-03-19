package com.fuj.fujitsuproject.windspeedfee;

import com.fuj.fujitsuproject.shared.service.VehicleAndWeatherBasedFeeService;
import com.fuj.fujitsuproject.windspeedfee.dto.WindSpeedFeeCreateDTO;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.weather.Weather;
import com.fuj.fujitsuproject.shared.exception.OverlappingWindSpeedFeeException;
import com.fuj.fujitsuproject.vehicle.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class WindSpeedFeeService extends VehicleAndWeatherBasedFeeService<WindSpeedFee, WindSpeedFeeRepository> {

    private final VehicleService vehicleService;

    public WindSpeedFeeService(WindSpeedFeeRepository windSpeedFeeRepository, VehicleService vehicleService) {
        super(windSpeedFeeRepository);
        this.vehicleService = vehicleService;
    }

    public List<WindSpeedFee> findAllWindSpeedFees() {
        return repository.findAll();
    }

    public WindSpeedFee createWindSpeedFee(WindSpeedFeeCreateDTO windSpeedFeeCreateDTO) {

        Vehicle vehicle = vehicleService
                .findVehicleById(windSpeedFeeCreateDTO.getVehicleId());

        BigDecimal minSpeed = windSpeedFeeCreateDTO.getMinSpeed();
        BigDecimal maxSpeed = windSpeedFeeCreateDTO.getMaxSpeed();

        List<WindSpeedFee> overlappingWindSpeedFees = repository
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

        return repository.save(windSpeedFee);
    }

    protected Optional<WindSpeedFee> findFeeByVehicleAndWeatherAndTime(
            Vehicle vehicle, Weather weather, LocalDateTime time) {

        return repository
                .findWindSpeedFeeByVehicleIdAndWindSpeedAndTime(
                        vehicle.getId(), weather.getWindSpeed(), time);
    }

    protected Optional<WindSpeedFee> findActiveFeeByVehicleAndWeather(
            Vehicle vehicle, Weather weather) {

        return repository
                .findLatestActiveWindSpeedFeeByVehicleIdAndSpeed(
                        vehicle.getId(), weather.getWindSpeed());

    }

}

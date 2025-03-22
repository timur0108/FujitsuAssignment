package com.fuj.fujitsuproject.windspeedfee;

import com.fuj.fujitsuproject.shared.service.VehicleAndWeatherBasedFeeService;
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

/**
 * Service for managing WindSpeedFeeEntities
 * This service extends {@link VehicleAndWeatherBasedFeeService}
 */
@Service
@Slf4j
public class WindSpeedFeeService extends VehicleAndWeatherBasedFeeService<WindSpeedFee, WindSpeedFeeRepository> {

    private final VehicleService vehicleService;
    private final WindSpeedFeeMapper mapper;

    public WindSpeedFeeService(WindSpeedFeeRepository windSpeedFeeRepository, VehicleService vehicleService,
                               WindSpeedFeeMapper mapper) {
        super(windSpeedFeeRepository);
        this.vehicleService = vehicleService;
        this.mapper = mapper;
    }


    /**
     * Creates and saves new WindSpeedFee entity.
     * Before adding a new wind speed fee checks if there is already active
     * wind speed fee with overlapping wind speed range.
     * @param windSpeedFeeCreateDTO the DTO containing wind speed fee details
     * @return the saved wind speed fee entity
     */
    public WindSpeedFee createWindSpeedFee(WindSpeedFeeCreateDTO windSpeedFeeCreateDTO) {

        Vehicle vehicle = vehicleService
                .findVehicleById(windSpeedFeeCreateDTO.getVehicleId());

        BigDecimal minSpeed = windSpeedFeeCreateDTO.getMinSpeed();
        BigDecimal maxSpeed = windSpeedFeeCreateDTO.getMaxSpeed();

        checkForOverlappingFees(minSpeed, maxSpeed, vehicle);

        WindSpeedFee windSpeedFee = mapper.toWindSpeedFee(windSpeedFeeCreateDTO, vehicle);

        return repository.save(windSpeedFee);
    }

    private void checkForOverlappingFees(BigDecimal minSpeed, BigDecimal maxSpeed, Vehicle vehicle) {
        List<WindSpeedFee> overlappingWindSpeedFees = repository
                .findActiveWindSpeedFeesWithOverlappingSpeedRangeByVehicleId(
                        minSpeed, maxSpeed, vehicle.getId());

        if (!overlappingWindSpeedFees.isEmpty()) throw new OverlappingWindSpeedFeeException(overlappingWindSpeedFees);

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

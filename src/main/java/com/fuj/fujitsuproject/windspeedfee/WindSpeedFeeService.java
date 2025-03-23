package com.fuj.fujitsuproject.windspeedfee;

import com.fuj.fujitsuproject.shared.exception.VehicleDeletedException;
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
     * Finds all wind speed fees from database. Used method of superclass.
     * @param activeOnly If active only is true then returns only those fees
     *                   that are currently active. Otherwise, returns all fees.
     * @return List of found wind speed fees as DTOs.
     */
    public List<WindSpeedFeeDTO> findAllWindSpeedFees(boolean activeOnly) {
        return findAllFees(activeOnly)
                .stream()
                .map(fee -> mapper.toDTO(fee))
                .toList();
    }

    /**
     * Creates and saves new WindSpeedFee entity.
     * Before adding a new wind speed fee checks if there is already active
     * wind speed fee with overlapping wind speed range.
     * @param windSpeedFeeCreateDTO the DTO containing wind speed fee details
     * @return the saved wind speed fee entity as DTO. If provided vehicle
     * is deleted the throws exception.
     */
    public WindSpeedFeeDTO createWindSpeedFee(WindSpeedFeeCreateDTO windSpeedFeeCreateDTO) {

        Vehicle vehicle = vehicleService
                .findVehicleById(windSpeedFeeCreateDTO.getVehicleId());
        if (vehicle.isDeleted()) throw new VehicleDeletedException("Couldn't create " +
                "new wind seed fee because provided vehicle is deleted.");

        BigDecimal minSpeed = windSpeedFeeCreateDTO.getMinSpeed();
        BigDecimal maxSpeed = windSpeedFeeCreateDTO.getMaxSpeed();

        checkForOverlappingFees(minSpeed, maxSpeed, vehicle);

        WindSpeedFee windSpeedFee = mapper.toWindSpeedFee(windSpeedFeeCreateDTO, vehicle);

        WindSpeedFee savedFee = repository.save(windSpeedFee);
        return mapper.toDTO(savedFee);
    }

    /**
     * Checks for wind speed fees with overlapping wind speed ranges. If such
     * fees are found then exception is thrown.
     * @param minSpeed minimum speed
     * @param maxSpeed maximum speed
     * @param vehicle vehicle to search for fees by.
     */
    private void checkForOverlappingFees(BigDecimal minSpeed, BigDecimal maxSpeed, Vehicle vehicle) {
        List<WindSpeedFee> overlappingWindSpeedFees = repository
                .findActiveWindSpeedFeesWithOverlappingSpeedRangeByVehicleId(
                        minSpeed, maxSpeed, vehicle.getId());

        if (!overlappingWindSpeedFees.isEmpty()) throw new OverlappingWindSpeedFeeException(overlappingWindSpeedFees);

    }

    /**
     * Implementation of abstract superclass method.
     * @param vehicle the vehicle to find fee for.
     * @param weather the weather to find fee for.
     * @param time the specific time for which the fee is to be found.
     * @return found WindSpeedFee
     */
    @Override
    protected Optional<WindSpeedFee> findFeeByVehicleAndWeatherAndTime(
            Vehicle vehicle, Weather weather, LocalDateTime time) {

        return repository
                .findWindSpeedFeeByVehicleIdAndWindSpeedAndTime(
                        vehicle.getId(), weather.getWindSpeed(), time);
    }

    /**
     * Implementation of abstract method from superclass.
     * @param vehicle the vehicle to find fee for
     * @param weather the weather to find fee for
     * @return found WindSpeedFee
     */
    @Override
    protected Optional<WindSpeedFee> findActiveFeeByVehicleAndWeather(
            Vehicle vehicle, Weather weather) {

        return repository
                .findLatestActiveWindSpeedFeeByVehicleIdAndSpeed(
                        vehicle.getId(), weather.getWindSpeed());

    }

}

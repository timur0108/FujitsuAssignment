package com.fuj.fujitsuproject.airtempfee;

import com.fuj.fujitsuproject.shared.service.VehicleAndWeatherBasedFeeService;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.weather.Weather;
import com.fuj.fujitsuproject.shared.exception.OverlappingAirTemperatureFeesException;
import com.fuj.fujitsuproject.vehicle.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing AirTemperatureFee entities.
 * This class extends {@link VehicleAndWeatherBasedFeeService}.
 */
@Slf4j
@Service
public class AirTemperatureFeeService extends VehicleAndWeatherBasedFeeService<AirTemperatureFee, AirTemperatureFeeRepository> {

    private final VehicleService vehicleService;
    private final AirTemperatureFeeMapper mapper;

    public AirTemperatureFeeService(AirTemperatureFeeRepository repository, VehicleService vehicleService,
                                    AirTemperatureFeeMapper mapper) {
        super(repository);
        this.vehicleService = vehicleService;
        this.mapper = mapper;
    }


    /**
     * Creates a new AirTemperatureFee based on the provided DTO.
     * This method checks for overlapping temperature ranges before creating the fee.
     * @param airTemperatureFeeCreateDTO The DTO containing the data to create a new AirTemperatureFee.
     * @return created AirTemperatureFee entity.
     */
    public AirTemperatureFee createAirTemperatureFee(AirTemperatureFeeCreateDTO airTemperatureFeeCreateDTO) {

        log.info("dto " + airTemperatureFeeCreateDTO.toString());
        Vehicle vehicle = vehicleService
                .findVehicleById(airTemperatureFeeCreateDTO.getVehicleId());
        if (vehicle.isDeleted()) throw new EntityNotFoundException("Provided vehicle is currently deleted");

        BigDecimal minTemperature = airTemperatureFeeCreateDTO.getMinTemperature();
        BigDecimal maxTemperature = airTemperatureFeeCreateDTO.getMaxTemperature();

        checkForOverlappingFees(vehicle, minTemperature, maxTemperature);

        AirTemperatureFee airTemperatureFee = mapper.toAirTemperatureFee(airTemperatureFeeCreateDTO, vehicle);
        return repository.save(airTemperatureFee);
    }

    /**
     * Checks for any overlapping temperature range fees for the given vehicle.
     * Throws an exception if any overlapping fees are found.
     * @param vehicle The vehicle for which the overlapping fees should be checked.
     * @param minTemperature The minimum temperature for the fee range.
     * @param maxTemperature The maximum temperature for the fee range.
     */
    private void checkForOverlappingFees(Vehicle vehicle, BigDecimal minTemperature, BigDecimal maxTemperature) {
        List<AirTemperatureFee> overlappingAirTemperatureFees = repository
                .findActiveAirTemperatureFeeWithOverlappingTemperatureRangeByVehicleId(
                                minTemperature, maxTemperature, vehicle.getId());
        if (!overlappingAirTemperatureFees.isEmpty()) {
            throw new OverlappingAirTemperatureFeesException(overlappingAirTemperatureFees);
        }
    }

    /**
     * Implementation of abstract method of superclass
     * @param vehicle the vehicle to find fee for.
     * @param weather the weather to find fee for.
     * @param time the specific time for which the fee is to be found.
     * @return
     */
    protected Optional<AirTemperatureFee> findFeeByVehicleAndWeatherAndTime(
            Vehicle vehicle, Weather weather, LocalDateTime time) {

        return repository
                .findAirTemperatureFeeByVehicleIdAndTemperatureAndTime(
                        vehicle.getId(), weather.getAirTemperature(), time);
    }

    /**
     * Implementation of abstract method of superclass
     * @param vehicle the vehicle to find fee for
     * @param weather the weather to find fee for
     * @return
     */
    protected Optional<AirTemperatureFee> findActiveFeeByVehicleAndWeather(
            Vehicle vehicle, Weather weather) {

        return repository
                .findLatestActiveAirTemperatureFeeByVehicleIdAndTemperature(
                        vehicle.getId(), weather.getAirTemperature());
    }

}

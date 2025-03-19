package com.fuj.fujitsuproject.airtempfee;

import com.fuj.fujitsuproject.shared.service.VehicleAndWeatherBasedFeeService;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.weather.Weather;
import com.fuj.fujitsuproject.shared.exception.OverlappingAirTemperatureFeesException;
import com.fuj.fujitsuproject.vehicle.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AirTemperatureFeeService extends VehicleAndWeatherBasedFeeService<AirTemperatureFee, AirTemperatureFeeRepository> {

    private final VehicleService vehicleService;

    public AirTemperatureFeeService(AirTemperatureFeeRepository repository, VehicleService vehicleService) {
        super(repository);
        this.vehicleService = vehicleService;
    }

    public List<AirTemperatureFee> findALlAirTemperatureFees() {
        return repository.findAll();
    }

    public AirTemperatureFee createAirTemperatureFee(
            AirTemperatureFeeCreateDTO airTemperatureFeeCreateDTO) {
        log.info("dto " + airTemperatureFeeCreateDTO.toString());
        Vehicle vehicle = vehicleService
                .findVehicleById(airTemperatureFeeCreateDTO.getVehicleId());

        BigDecimal minTemperature = airTemperatureFeeCreateDTO.getMinTemperature();
        BigDecimal maxTemperature = airTemperatureFeeCreateDTO.getMaxTemperature();

        List<AirTemperatureFee> overlappingAirTemperatureFees =
                repository
                        .findActiveAirTemperatureFeeWithOverlappingTemperatureRangeByVehicleId(
                                minTemperature, maxTemperature, vehicle.getId());
        log.info("overlap " + overlappingAirTemperatureFees.toString());

        if (!overlappingAirTemperatureFees.isEmpty()) {
            throw new OverlappingAirTemperatureFeesException(overlappingAirTemperatureFees);
        }

        AirTemperatureFee airTemperatureFee = new AirTemperatureFee();
        airTemperatureFee.setMinTemperature(minTemperature);
        airTemperatureFee.setMaxTemperature(maxTemperature);
        airTemperatureFee.setVehicle(vehicle);
        airTemperatureFee.setActive(true);
        airTemperatureFee.setAmount(airTemperatureFeeCreateDTO.getAmount());
        airTemperatureFee.setForbidden(airTemperatureFeeCreateDTO.isForbidden());

        return repository.save(airTemperatureFee);
    }

    protected Optional<AirTemperatureFee> findFeeByVehicleAndWeatherAndTime(
            Vehicle vehicle, Weather weather, LocalDateTime time) {

        return repository
                .findAirTemperatureFeeByVehicleIdAndTemperatureAndTime(
                        vehicle.getId(), weather.getAirTemperature(), time);
    }

    protected Optional<AirTemperatureFee> findActiveFeeByVehicleAndWeather(
            Vehicle vehicle, Weather weather) {

        return repository
                .findLatestActiveAirTemperatureFeeByVehicleIdAndTemperature(
                        vehicle.getId(), weather.getAirTemperature());
    }

}

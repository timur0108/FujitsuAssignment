package com.fuj.fujitsuproject.airtempfee;

import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.weather.Weather;
import com.fuj.fujitsuproject.shared.exception.AirTemperatureFeeAlreadyInactiveException;
import com.fuj.fujitsuproject.shared.exception.OverlappingAirTemperatureFeesException;
import com.fuj.fujitsuproject.shared.exception.VehicleForbiddenException;
import com.fuj.fujitsuproject.vehicle.VehicleService;
import com.fuj.fujitsuproject.shared.service.WeatherBasedFeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirTemperatureFeeService implements WeatherBasedFeeService {

    private final AirTemperatureFeeRepository airTemperatureFeeRepository;

    private final VehicleService vehicleService;

    public List<AirTemperatureFee> findALlAirTemperatureFees() {
        return airTemperatureFeeRepository.findAll();
    }

    public void deactivateAirTemperatureFee(Long id) {

        AirTemperatureFee airTemperatureFee = airTemperatureFeeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Couldn't find " +
                        "air temperature fee with id=" + id));

        if (!airTemperatureFee.isActive()) throw new AirTemperatureFeeAlreadyInactiveException();

        airTemperatureFee.setActive(false);
        airTemperatureFee.setDeactivatedAt(LocalDateTime.now());

        airTemperatureFeeRepository.save(airTemperatureFee);
    }

    public AirTemperatureFee createAirTemperatureFee(
            AirTemperatureFeeCreateDTO airTemperatureFeeCreateDTO) {
        log.info("dto " + airTemperatureFeeCreateDTO.toString());
        Vehicle vehicle = vehicleService
                .findVehicleById(airTemperatureFeeCreateDTO.getVehicleId());

        BigDecimal minTemperature = airTemperatureFeeCreateDTO.getMinTemperature();
        BigDecimal maxTemperature = airTemperatureFeeCreateDTO.getMaxTemperature();

        List<AirTemperatureFee> overlappingAirTemperatureFees =
                airTemperatureFeeRepository
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

        return airTemperatureFeeRepository.save(airTemperatureFee);
    }

    private Optional<AirTemperatureFee> findAirTemperatureFeeByVehicleAndWeatherAndTime(
            Vehicle vehicle, Weather weather, LocalDateTime time) {

        return airTemperatureFeeRepository
                .findAirTemperatureFeeByVehicleIdAndTemperatureAndTime(
                        vehicle.getId(), weather.getAirTemperature(), time);
    }

    private Optional<AirTemperatureFee> findLatestActiveAirTemperatureFeeByVehicleAndWeather(
            Vehicle vehicle, Weather weather) {

        return airTemperatureFeeRepository
                .findLatestActiveAirTemperatureFeeByVehicleIdAndTemperature(
                        vehicle.getId(), weather.getAirTemperature());
    }

    //тут используются методы репозитории а не сервиса. Исправить или так и оставить.
    @Override
    public BigDecimal calculateFee(Vehicle vehicle, Weather weather, Optional<LocalDateTime> time) {

        Optional<AirTemperatureFee> airTemperatureFeeOptional;

        if (time.isPresent()) airTemperatureFeeOptional = airTemperatureFeeRepository
                .findAirTemperatureFeeByVehicleIdAndTemperatureAndTime(vehicle.getId(), weather.getAirTemperature(), time.get());
        else airTemperatureFeeOptional = airTemperatureFeeRepository
                .findLatestActiveAirTemperatureFeeByVehicleIdAndTemperature(vehicle.getId(), weather.getAirTemperature());

        if (airTemperatureFeeOptional.isEmpty()) return BigDecimal.ZERO;

        AirTemperatureFee fee = airTemperatureFeeOptional.get();
        if (fee.isForbidden()) throw new VehicleForbiddenException();

        log.info("air temperature fee=" + fee.getAmount().toString());
        return fee.getAmount();

    }
}

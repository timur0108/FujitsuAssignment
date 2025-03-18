package com.fuj.fujitsuproject.domain.weatherphenomenonfee;

import com.fuj.fujitsuproject.domain.vehicle.Vehicle;
import com.fuj.fujitsuproject.domain.vehicle.VehicleService;
import com.fuj.fujitsuproject.domain.weather.Weather;
import com.fuj.fujitsuproject.exception.FeeAlreadyInactiveException;
import com.fuj.fujitsuproject.exception.OverlappingWeatherPhenomenon;
import com.fuj.fujitsuproject.exception.VehicleForbiddenException;
import com.fuj.fujitsuproject.service.WeatherBasedFeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherPhenomenonFeeService implements WeatherBasedFeeService {

    private final WeatherPhenomenonFeeRepository weatherPhenomenonFeeRepository;
    private final VehicleService vehicleService;

    private Optional<WeatherPhenomenonFee> findWpefByVehicleAndWeatherAndTime(
            Vehicle vehicle, Weather weather, LocalDateTime time) {

        return weatherPhenomenonFeeRepository
                .findWeatherPhenomenonFeeByVehicleIdAndPhenomenonAndTime(
                        vehicle.getId(), weather.getWeatherPhenomenon().toLowerCase(), time);
    }

    private Optional<WeatherPhenomenonFee> findLatestWpefByVehicleAndWeather(Vehicle vehicle, Weather weather) {
        log.info("weather phenomenon=" + weather.getWeatherPhenomenon());
        return weatherPhenomenonFeeRepository
                .findLatestWeatherPhenomenonFeeByVehicleIdAndPhenomenon(vehicle.getId(), weather.getWeatherPhenomenon().toLowerCase());
    }

    public List<WeatherPhenomenonFee> findAllFees() {
        return weatherPhenomenonFeeRepository.findAll();
    }

    public WeatherPhenomenonFee createWeatherPhenomenonFee(
            WeatherPhenomenonFeeCreateDTO weatherPhenomenonFeeCreateDTO
    ) {

        Vehicle vehicle = vehicleService.findVehicleById(weatherPhenomenonFeeCreateDTO.getVehicleId());

        String phenomenon = weatherPhenomenonFeeCreateDTO.getPhenomenon();

        Optional<WeatherPhenomenonFee> existingWeatherPhenomenonFee =
                weatherPhenomenonFeeRepository
                        .findWeatherPhenomenonFeeByPhenomenonAndActive(phenomenon);

        if (existingWeatherPhenomenonFee.isPresent()) throw new OverlappingWeatherPhenomenon(phenomenon);

        WeatherPhenomenonFee weatherPhenomenonFee = new WeatherPhenomenonFee();
        weatherPhenomenonFee.setPhenomenon(phenomenon);
        weatherPhenomenonFee.setAmount(weatherPhenomenonFeeCreateDTO.getAmount());
        weatherPhenomenonFee.setForbidden(weatherPhenomenonFeeCreateDTO.isForbidden());
        weatherPhenomenonFee.setVehicle(vehicle);
        weatherPhenomenonFee.setActive(true);

        return weatherPhenomenonFeeRepository.save(weatherPhenomenonFee);
    }

    @Transactional
    public void deactivateWeatherPhenomenonFee(Long id) {

        WeatherPhenomenonFee weatherPhenomenonFee = weatherPhenomenonFeeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("" +
                        "Couldn't find weather phenomenon fee."));

        if (!weatherPhenomenonFee.isActive()) throw new FeeAlreadyInactiveException();

        weatherPhenomenonFee.setActive(false);
        weatherPhenomenonFee.setDeactivatedAt(LocalDateTime.now());

        weatherPhenomenonFeeRepository.save(weatherPhenomenonFee);
    }

    @Override
    public BigDecimal calculateFee(Vehicle vehicle, Weather weather, Optional<LocalDateTime> time) {
        Optional<WeatherPhenomenonFee> weatherPhenomenonFeeOptional;

        if (time.isPresent()) weatherPhenomenonFeeOptional = findWpefByVehicleAndWeatherAndTime(
                vehicle, weather, time.get());
        else weatherPhenomenonFeeOptional = findLatestWpefByVehicleAndWeather(vehicle, weather);

        if (weatherPhenomenonFeeOptional.isEmpty()) return BigDecimal.ZERO;

        WeatherPhenomenonFee weatherPhenomenonFee = weatherPhenomenonFeeOptional.get();
        if (weatherPhenomenonFee.isForbidden()) throw new VehicleForbiddenException();

        log.info("phenomenon fee=" + weatherPhenomenonFee.getAmount().toString());
        return weatherPhenomenonFee.getAmount();
    }
}

package com.fuj.fujitsuproject.weatherphenomenonfee;

import com.fuj.fujitsuproject.shared.service.VehicleAndWeatherBasedFeeService;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.vehicle.VehicleService;
import com.fuj.fujitsuproject.weather.Weather;
import com.fuj.fujitsuproject.shared.exception.OverlappingWeatherPhenomenon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing WeatherPhenomenonFee entities.
 * This class extends {@link VehicleAndWeatherBasedFeeService}.
 */
@Slf4j
@Service
public class WeatherPhenomenonFeeService extends VehicleAndWeatherBasedFeeService<WeatherPhenomenonFee, WeatherPhenomenonFeeRepository> {

    protected final VehicleService vehicleService;
    private final WeatherPhenomenonFeeMapper mapper;

    public WeatherPhenomenonFeeService(WeatherPhenomenonFeeRepository weatherPhenomenonFeeRepository,
                                       VehicleService vehicleService, WeatherPhenomenonFeeMapper mapper) {
        super(weatherPhenomenonFeeRepository);
        this.vehicleService = vehicleService;
        this.mapper = mapper;
    }

    protected Optional<WeatherPhenomenonFee> findFeeByVehicleAndWeatherAndTime(
            Vehicle vehicle, Weather weather, LocalDateTime time) {

        return repository
                .findWeatherPhenomenonFeeByVehicleIdAndPhenomenonAndTime(
                        vehicle.getId(), weather.getWeatherPhenomenon().toLowerCase(), time);
    }

    protected Optional<WeatherPhenomenonFee> findActiveFeeByVehicleAndWeather(Vehicle vehicle, Weather weather) {
        log.info("weather phenomenon=" + weather.getWeatherPhenomenon());
        return repository
                .findLatestWeatherPhenomenonFeeByVehicleIdAndPhenomenon(vehicle.getId(), weather.getWeatherPhenomenon().toLowerCase());
    }


    /**
     * Creates a new WeatherPhenomenonFee based on the provided DTO.
     * This method checks for overlapping phenomenon names before creating the fee.
     * @param weatherPhenomenonFeeCreateDTO The DTO containing the data to create a new WeatherPhenomenonFee
     * @return created WeatherPhenomenonFee entity
     */
    public WeatherPhenomenonFee createWeatherPhenomenonFee(
            WeatherPhenomenonFeeCreateDTO weatherPhenomenonFeeCreateDTO
    ) {

        Vehicle vehicle = vehicleService.findVehicleById(weatherPhenomenonFeeCreateDTO.getVehicleId());

        String phenomenon = weatherPhenomenonFeeCreateDTO.getPhenomenon();
        checkForOverlappingFees(phenomenon, vehicle);

        WeatherPhenomenonFee weatherPhenomenonFee = mapper.toWeatherPhenomenonFee(
                weatherPhenomenonFeeCreateDTO, vehicle);

        return repository.save(weatherPhenomenonFee);
    }

    private void checkForOverlappingFees(String phenomenon, Vehicle vehicle) {
        List<WeatherPhenomenonFee> existingWeatherPhenomenonFee =
                repository
                        .findWeatherPhenomenonFeeByPhenomenonAndVehicleIdAndActiveTrue(
                                phenomenon.toLowerCase(), vehicle.getId());

        if (!existingWeatherPhenomenonFee.isEmpty()) {
            throw new OverlappingWeatherPhenomenon(phenomenon, existingWeatherPhenomenonFee);
        }
    }
}

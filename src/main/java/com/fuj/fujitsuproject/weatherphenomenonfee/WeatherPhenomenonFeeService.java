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

@Slf4j
@Service
public class WeatherPhenomenonFeeService extends VehicleAndWeatherBasedFeeService<WeatherPhenomenonFee, WeatherPhenomenonFeeRepository> {

    protected final VehicleService vehicleService;

    public WeatherPhenomenonFeeService(WeatherPhenomenonFeeRepository weatherPhenomenonFeeRepository,
                                       VehicleService vehicleService) {
        super(weatherPhenomenonFeeRepository);
        this.vehicleService = vehicleService;
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

    public List<WeatherPhenomenonFee> findAllFees() {
        return repository.findAll();
    }

    public WeatherPhenomenonFee createWeatherPhenomenonFee(
            WeatherPhenomenonFeeCreateDTO weatherPhenomenonFeeCreateDTO
    ) {

        Vehicle vehicle = vehicleService.findVehicleById(weatherPhenomenonFeeCreateDTO.getVehicleId());

        String phenomenon = weatherPhenomenonFeeCreateDTO.getPhenomenon();

        //поиск по транспорту ещё должен быть
        Optional<WeatherPhenomenonFee> existingWeatherPhenomenonFee =
                repository
                        .findWeatherPhenomenonFeeByPhenomenonAndVehicleIdAndActiveTrue(phenomenon, vehicle.getId());

        if (existingWeatherPhenomenonFee.isPresent()) throw new OverlappingWeatherPhenomenon(phenomenon);

        WeatherPhenomenonFee weatherPhenomenonFee = new WeatherPhenomenonFee();
        weatherPhenomenonFee.setPhenomenon(phenomenon);
        weatherPhenomenonFee.setAmount(weatherPhenomenonFeeCreateDTO.getAmount());
        weatherPhenomenonFee.setForbidden(weatherPhenomenonFeeCreateDTO.isForbidden());
        weatherPhenomenonFee.setVehicle(vehicle);
        weatherPhenomenonFee.setActive(true);

        return repository.save(weatherPhenomenonFee);
    }

}

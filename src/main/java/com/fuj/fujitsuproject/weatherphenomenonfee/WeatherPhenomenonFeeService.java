package com.fuj.fujitsuproject.weatherphenomenonfee;

import com.fuj.fujitsuproject.shared.exception.VehicleDeletedException;
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

    /**
     * Finds all weather phenomenon fees from database. Uses method of abstract superclass.
     * @param activeOnly If activeOnly is true then searches only for those fees
     *                   that are currently active. Otherwise, searches for all fees.
     * @return List of weather phenomenon fees ad DTOs.
     */
    public List<WeatherPhenomenonFeeDTO> findALlWeatherPhenomenonFees(boolean activeOnly) {
        return findAllFees(activeOnly)
                .stream()
                .map(fee -> mapper.toDTO(fee))
                .toList();
    }

    /**
     * Implementation of method from abstract superclass
     * @param vehicle the vehicle to find fee for.
     * @param weather the weather to find fee for.
     * @param time the specific time for which the fee is to be found.
     * @return found WeatherPhenomenonFee
     */
    @Override
    protected Optional<WeatherPhenomenonFee> findFeeByVehicleAndWeatherAndTime(
            Vehicle vehicle, Weather weather, LocalDateTime time) {

        return repository
                .findWeatherPhenomenonFeeByVehicleIdAndPhenomenonAndTime(
                        vehicle.getId(), weather.getWeatherPhenomenon().toLowerCase(), time);
    }

    /**
     * Implementation of method from abstract superclass.
     * @param vehicle the vehicle to find fee for
     * @param weather the weather to find fee for
     * @return found WeatherPhenomenonFee
     */
    @Override
    protected Optional<WeatherPhenomenonFee> findActiveFeeByVehicleAndWeather(Vehicle vehicle, Weather weather) {
        log.info("weather phenomenon=" + weather.getWeatherPhenomenon());
        return repository
                .findLatestWeatherPhenomenonFeeByVehicleIdAndPhenomenon(vehicle.getId(), weather.getWeatherPhenomenon().toLowerCase());
    }


    /**
     * Creates a new WeatherPhenomenonFee based on the provided DTO.
     * This method checks for overlapping phenomenon names before creating the fee.
     * @param weatherPhenomenonFeeCreateDTO The DTO containing the data to create a new WeatherPhenomenonFee
     * @return created WeatherPhenomenonFee entity. If provided vehicle is deleted throws exception.
     */
    public WeatherPhenomenonFeeDTO createWeatherPhenomenonFee(
            WeatherPhenomenonFeeCreateDTO weatherPhenomenonFeeCreateDTO
    ) {

        Vehicle vehicle = vehicleService.findVehicleById(weatherPhenomenonFeeCreateDTO.getVehicleId());
        if (vehicle.isDeleted()) throw new VehicleDeletedException("" +
                "Couldn't create new weather phenomenon fee because provided " +
                "vehicle is deleted.");
        String phenomenon = weatherPhenomenonFeeCreateDTO.getPhenomenon();
        checkForOverlappingFees(phenomenon, vehicle);

        WeatherPhenomenonFee weatherPhenomenonFee = mapper.toWeatherPhenomenonFee(
                weatherPhenomenonFeeCreateDTO, vehicle);

        WeatherPhenomenonFee savedFee = repository.save(weatherPhenomenonFee);
        return mapper.toDTO(savedFee);
    }

    /**
     * Checks for active weather phenomenon fees with the same phenomenon name.
     * If found throws exception.
     * @param phenomenon phenomenon name to search by.
     * @param vehicle vehicle to search by
     */
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

package com.fuj.fujitsuproject.weather;

import com.fuj.fujitsuproject.city.City;
import com.fuj.fujitsuproject.city.CityService;
import com.fuj.fujitsuproject.shared.utils.TimeUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final CityService cityService;
    private final ObservationDataToWeatherMapper mapper;

    /**
     * Processes the observations data received from external API and stores it.
     * @param observations observations data that needs to be saved.
     */
    public void saveObservationData(Observations observations) {
        log.info("Saving observation data.");

        List<String> stations = cityService.getAllStations();
        if (stations.isEmpty()) {
            log.info("Couldn't fetch stations from database");
            return;
        }
        log.info("needed stations: {}", stations);

        List<Weather> weatherData = mapper.toWeatherList(observations, stations);
        log.info("Fetched weather data: {}", weatherData);

        weatherRepository.saveAll(weatherData);
    }

    /**
     * Searches for weather record for provided city and optional time.
     * @param city City to search for weather by.
     * @param time If time is provided then searches for weather records at the provided time.
     *             Otherwise, uses current time to search for weather.
     * @return Found weather. Throws exception if no weather is found.
     */
    public Weather findWeatherByCityAndTime(City city, Optional<LocalDateTime> time) {

        LocalDateTime timeToSearchBy = time.orElseGet(LocalDateTime::now);
        return weatherRepository
                .findWeatherByStationAndTime(city.getStationName(), timeToSearchBy)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find weather data for station=" + city.getStationName() + ", "
                                + " time=" + time));
    }


}

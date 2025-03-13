package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.entity.City;
import com.fuj.fujitsuproject.entity.Weather;
import com.fuj.fujitsuproject.model.Observations;
import com.fuj.fujitsuproject.repository.WeatherRepository;
import com.fuj.fujitsuproject.utils.TimeUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final CityService cityService;

    public void saveObservationData(Observations observations) {
        log.info("Saving observation data.");

        LocalDateTime observationTime = TimeUtils
                .convertUnix2LocalDateTime(observations.getTimestamp());

        List<String> stations = cityService.getAllStations();
        if (stations.isEmpty()) {
            log.info("Couldn't fetch stations from database");
            return;
        }
        // мб сделать так чтобы проверялось нашлись ли данные для каждой станции.
        log.info("needed stations: {}", stations);

        List<Weather> weatherData = observations
                .getStations()
                .stream()
                .filter(station -> stations.contains(station.getName()))
                .map(station -> {
                    Weather weather = new Weather();
                    weather.setObservationTime(observationTime);
                    weather.setWeatherPhenomenon(station.getPhenomenon());
                    weather.setAirTemperature(station.getAirTemperature());
                    weather.setWindSpeed(station.getWindSpeed());
                    weather.setStationName(station.getName());
                    weather.setWmoCode(station.getWmoCode());
                    return weather;
                }).toList();
        log.info("Fetched weather data: {}", weatherData);

        weatherRepository.saveAll(weatherData);
    }

    public Weather findWeatherByCityAndTime(City city, LocalDateTime time) {
        return weatherRepository
                .findWeatherByStationAndTime(city.getStationName(), time)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find weather data for station=" + city.getStationName() + ", "
                                + " time=" + time));
    }
}

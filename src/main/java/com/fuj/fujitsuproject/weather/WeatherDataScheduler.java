package com.fuj.fujitsuproject.weather;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled component that periodically fetches data from external API using the
 * WeatherDataFetcher client. After that saves data using WeatherService.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherDataScheduler {

    private final WeatherDataFetcher weatherDataFetcher;
    private final WeatherService weatherService;

    @Scheduled(cron = "${scheduler.cron}")
    public void getWeatherData() {

        Observations observations;
        try {
            observations = weatherDataFetcher.fetchWeatherData();
        } catch (FeignException e) {
            log.info("Couldn't fetch weather data. {}", e.getMessage());
            return;
        }

        if (observations == null || observations.getStations().isEmpty()) {
            log.info("Fetched empty data");
            return;
        }

        weatherService.saveObservationData(observations);
    }
}

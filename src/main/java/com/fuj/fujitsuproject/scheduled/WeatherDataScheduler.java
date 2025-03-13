package com.fuj.fujitsuproject.scheduled;

import com.fasterxml.jackson.databind.ser.std.TokenBufferSerializer;
import com.fuj.fujitsuproject.client.WeatherDataFetcher;
import com.fuj.fujitsuproject.entity.Weather;
import com.fuj.fujitsuproject.model.Observations;
import com.fuj.fujitsuproject.service.CityService;
import com.fuj.fujitsuproject.service.WeatherService;
import com.fuj.fujitsuproject.utils.TimeUtils;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

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

        if (observations == null || observations.getStations() == null) {
            log.info("Fetched empty data");
            return;
        }

        weatherService.saveObservationData(observations);
    }

//    @Scheduled(cron = "${scheduler.cron}")
//    public void getWeatherData() {
//        Observations observations = weatherDataFetcher.fetchWeatherData();
//
//        LocalDateTime observationTime = LocalDateTime
//                .ofInstant(Instant.ofEpochSecond(observations.getTimestamp()),
//                        TimeZone.getDefault().toZoneId());
//
//        List<Weather> weatherData = new ArrayList<>();
//
//        observations.getStations().stream()
//                .filter(station -> station.getName().equals("Tallinn-Harku")
//                || station.getName().equals("Tartu-Tõravere")
//                || station.getName().equals("Pärnu"))
//                .map(station -> {
//                    Weather weather = new Weather();
//                    weather.setObservationTime(observationTime);
//                    weather.setWeatherPhenomenon(station.getPhenomenon());
//                    weather.setAirTemperature(station.getAirTemperature());
//                    weather.setWindSpeed(station.getWindSpeed());
//                    weather.setStationName(station.getName());
//                    weather.setWmoCode(station.getWmoCode());
//                    return weather;
//                }).forEach(weatherData::add);
//
//        System.out.println(weatherData);
//    }
}

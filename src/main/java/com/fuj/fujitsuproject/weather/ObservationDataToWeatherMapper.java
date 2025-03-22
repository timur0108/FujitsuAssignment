package com.fuj.fujitsuproject.weather;

import com.fuj.fujitsuproject.city.City;
import com.fuj.fujitsuproject.shared.utils.TimeUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ObservationDataToWeatherMapper {

    public List<Weather> toWeatherList(Observations observations, List<String> stations) {

        LocalDateTime observationTime = TimeUtils
                .convertUnix2LocalDateTime(observations.getTimestamp());

        return observations.getStations()
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
                })
                .toList();
    }
}

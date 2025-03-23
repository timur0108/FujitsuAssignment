package com.fuj.fujitsuproject.weather;

import com.fuj.fujitsuproject.ProjectConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Feign client that is used to fetch weather data from external API.
 * It retrieves information in XML format and maps them to Observations object.
 */
@FeignClient(name = "weather", url = "${weather.api.url}", configuration = ProjectConfig.class)
public interface WeatherDataFetcher {

    @GetMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    Observations fetchWeatherData();
}

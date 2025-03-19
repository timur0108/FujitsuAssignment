package com.fuj.fujitsuproject.weather;

import com.fuj.fujitsuproject.ProjectConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "weather", url = "${weather.api.url}", configuration = ProjectConfig.class)
public interface WeatherDataFetcher {

    @GetMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    Observations fetchWeatherData();
}

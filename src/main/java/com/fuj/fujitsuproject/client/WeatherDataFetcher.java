package com.fuj.fujitsuproject.client;

import com.fuj.fujitsuproject.ProjectConfig;
import com.fuj.fujitsuproject.model.Observations;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "weather", url = "${weather.api.url}", configuration = ProjectConfig.class)
public interface WeatherDataFetcher {

    @GetMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    Observations fetchWeatherData();
}

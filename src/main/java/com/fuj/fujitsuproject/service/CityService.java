package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.entity.City;
import com.fuj.fujitsuproject.repository.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<String> getAllStations() {
        return cityRepository.findAllStationName();
    }

    public City findCityByName(String cityName) {
        return cityRepository
                .findByNameEquals(cityName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find city with name=" + cityName));
    }
}

package com.fuj.fujitsuproject.domain.city;

import com.fuj.fujitsuproject.exception.CityAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public City findCityById(Long id) {
        return cityRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find city with id=" + id));
    }

    public List<String> getAllStations() {
        return cityRepository.findAllStationName();
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City addCity(CityCreateDTO cityCreateDTO) {

        Optional<City> existingCity = cityRepository.findByNameEquals(cityCreateDTO.getName());

        if (existingCity.isPresent()) throw new CityAlreadyExistsException();

        City city = new City();
        city.setName(cityCreateDTO.getName());
        city.setStationName(cityCreateDTO.getStationName());

        return cityRepository.save(city);
    }

    public City findCityByName(String cityName) {
        return cityRepository
                .findByNameEquals(cityName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find city with name=" + cityName));
    }

    public void deleteCityById(Long id) {

        City city = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Couldn't find " +
                        "city to delete"));

        cityRepository.delete(city);
    }

    public City updateCityById(Long id, CityCreateDTO cityCreateDTO) {

        City city = cityRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Couldn't find city to update"));

        city.setStationName(cityCreateDTO.getStationName());
        city.setName(cityCreateDTO.getName());

        return cityRepository.save(city);
    }
}

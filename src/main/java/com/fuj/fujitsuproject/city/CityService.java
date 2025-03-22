package com.fuj.fujitsuproject.city;

import com.fuj.fujitsuproject.regionalbasefee.RegionalBaseFeeService;
import com.fuj.fujitsuproject.shared.exception.CityAlreadyDeletedException;
import com.fuj.fujitsuproject.shared.exception.CityAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CityService {

    private final CityRepository cityRepository;
    private final CityMapper mapper;
    private RegionalBaseFeeService regionalBaseFeeService;

    @Autowired
    public void setRegionalBaseFeeService(@Lazy RegionalBaseFeeService service) {
        this.regionalBaseFeeService = service;
    }

    @Autowired
    public CityService(CityRepository cityRepository, CityMapper mapper) {
        this.cityRepository = cityRepository;
        this.mapper = mapper;
    }

    public City findCityById(Long id) {
        return cityRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find city with id=" + id));
    }

    public List<String> getAllStations() {
        return cityRepository.findAllStationName();
    }

    public List<City> getAllCities(boolean notDeletedOnly) {
        if (notDeletedOnly) return cityRepository.findAllByDeletedFalse();
        return cityRepository.findAll();
    }

    public City addCity(CityCreateDTO cityCreateDTO) {
        Optional<City> existingCity = cityRepository.findCityByNameEqualsAndDeletedFalse(cityCreateDTO.getName());

        if (existingCity.isPresent()) throw new CityAlreadyExistsException();

        City city = mapper.toCity(cityCreateDTO);
        return cityRepository.save(city);
    }

    public City findActiveCityByName(String name) {
        return cityRepository.findCityByNameEqualsAndDeletedFalse(name)
                .orElseThrow(() -> new EntityNotFoundException("" +
                        "Couldn't find currently active city with name=" + name));
    }

    public City findCityByNameAndTime(String name, Optional<LocalDateTime> time) {
        if (!time.isPresent()) return findActiveCityByName(name);
        return cityRepository.findCityByNameAndTime(name, time.get())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find city with name=" + name + " that was active at " + time.get()
                ));
    }

    @Transactional
    public void deleteCityById(Long id) {
        City city = findCityById(id);
        if (city.isDeleted()) throw new CityAlreadyDeletedException(id);
        city.setDeleted(true);
        city.setDeletedAt(LocalDateTime.now());
        regionalBaseFeeService.deactivateRegionalBaseFeesByCity(city);
        cityRepository.save(city);
    }

    public City updateCityById(Long id, CityCreateDTO cityCreateDTO) {

        City city = cityRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Couldn't find city to update"));

        city.setStationName(cityCreateDTO.getStationName());
        city.setName(cityCreateDTO.getName());

        return cityRepository.save(city);
    }
}

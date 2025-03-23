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

    /**
     * Method to find City entity by id by calling repository layer.
     * @param id id to search for City by.
     * @return returns City that was found or throws exception if no City was found.
     */
    public City findCityById(Long id) {
        return cityRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find city with id=" + id));
    }

    /**
     * Method to find all station names for not deleted cities.
     * @return List of station names.
     */
    public List<String> getAllStations() {
        return cityRepository.findAllStationName();
    }

    /**
     * Method for finding all cities or all cities that are not marked as deleted.
     * @param notDeletedOnly If notDeletedOnly equals true, then finds only
     *                       those cities that are not marked as deleted. Otherwise
     *                       returns all citites.
     * @return returns found cities as DTOs.
     */
    public List<CityDTO> getAllCities(boolean notDeletedOnly) {
        List<City> foundCities;
        if (notDeletedOnly) foundCities = cityRepository.findAllByDeletedFalse();
        else foundCities = cityRepository.findAll();
        return foundCities
                .stream()
                .map(city -> mapper.toDTO(city))
                .toList();
    }

    /**
     * Method for adding new city to database.
     * @param cityCreateDTO DTO that contains all the needed data to create new City entity.
     * @return returns newly saved City entity as DTO. Throws exception if a city with
     * such name and is not deleted already exists.
     */
    public CityDTO addCity(CityCreateDTO cityCreateDTO) {
        Optional<City> existingCity = cityRepository.findCityByNameEqualsAndDeletedFalse(cityCreateDTO.getName());

        if (existingCity.isPresent()) throw new CityAlreadyExistsException();

        City city = mapper.toCity(cityCreateDTO);
        City savedCity = cityRepository.save(city);
        return mapper.toDTO(savedCity);
    }

    /**
     * Finds city from repository layer by name and which is not deleted.
     * @param name Name to search for city by.
     * @return returns found City. Throws exception if no city was found.
     */
    public City findActiveCityByName(String name) {
        return cityRepository.findCityByNameEqualsAndDeletedFalse(name)
                .orElseThrow(() -> new EntityNotFoundException("" +
                        "Couldn't find currently active city with name=" + name));
    }

    /**
     * Finds city by name and time if provided.
     * @param name Name to search city by.
     * @param time optional time. If proved then search for city that existed at the time.
     * @return returns found city. Throws exception if no city was found.
     */
    public City findCityByNameAndTime(String name, Optional<LocalDateTime> time) {
        if (!time.isPresent()) return findActiveCityByName(name);
        return cityRepository.findCityByNameAndTime(name, time.get())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find city with name=" + name + " that was active at " + time.get()
                ));
    }

    /**
     * Method for deleting city by its id.
     * After deleting the city deactivated all regional base fees that were connected
     * to that city.
     * @param id ID to delete city by.
     *           Throws exception if city is already marked as deleted.
     */
    @Transactional
    public void deleteCityById(Long id) {
        City city = findCityById(id);
        if (city.isDeleted()) throw new CityAlreadyDeletedException(id);
        city.setDeleted(true);
        city.setDeletedAt(LocalDateTime.now());
        regionalBaseFeeService.deactivateRegionalBaseFeesByCity(city);
        cityRepository.save(city);
    }

    /**
     * Method for updating existing city.
     * @param id id to search city by.
     * @param cityCreateDTO DTO containing all the needed data to update city.
     * @return returns updayed City entity as DTO.
     */
    public CityDTO updateCityById(Long id, CityCreateDTO cityCreateDTO) {

        City city = findCityById(id);
        city.setStationName(cityCreateDTO.getStationName());
        city.setName(cityCreateDTO.getName());
        City savedCity = cityRepository.save(city);
        return mapper.toDTO(savedCity);
    }
}

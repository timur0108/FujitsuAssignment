package com.fuj.fujitsuproject.city;

import org.springframework.stereotype.Component;

/**
 * Class that provided methods to convert DTO to City object.
 */
@Component
public class CityMapper {

    /**
     * Method for converting DTO to City object.
     * @param dto provided DTO that contains needed data to convert to City object.
     * @return
     */
    public City toCity(CityCreateDTO dto) {
        City city = new City();
        city.setName(dto.getName());
        city.setDeleted(false);
        city.setStationName(dto.getStationName());
        return city;
    }

    /**
     * Method for converting City object to DTO.
     * @param city provided City object to be converted to DTO.
     * @return DTO resulted of converting from City object to DTO.
     */
    public CityDTO toDTO(City city) {
        CityDTO dto = new CityDTO();
        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setStationName(city.getStationName());
        dto.setDeleted(city.isDeleted());
        dto.setDeletedAt(city.getDeletedAt());
        dto.setCreatedAt(city.getCreatedAt());
        return dto;
    }
}

package com.fuj.fujitsuproject.city;

import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    public City toCity(CityCreateDTO dto) {
        City city = new City();
        city.setName(dto.getName());
        city.setDeleted(false);
        city.setStationName(dto.getStationName());
        return city;
    }
}

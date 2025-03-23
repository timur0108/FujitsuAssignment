package com.fuj.fujitsuproject.weatherphenomenonfee;

import com.fuj.fujitsuproject.vehicle.Vehicle;
import org.springframework.stereotype.Component;

/**
 * Class for mapping WeatherPhenomenon to and from DTO.
 */
@Component
public class WeatherPhenomenonFeeMapper {

    public WeatherPhenomenonFee toWeatherPhenomenonFee(WeatherPhenomenonFeeCreateDTO dto, Vehicle vehicle) {

        WeatherPhenomenonFee weatherPhenomenonFee = new WeatherPhenomenonFee();
        weatherPhenomenonFee.setPhenomenon(dto.getPhenomenon());
        weatherPhenomenonFee.setAmount(dto.getAmount());
        weatherPhenomenonFee.setForbidden(dto.isForbidden());
        weatherPhenomenonFee.setVehicle(vehicle);
        weatherPhenomenonFee.setActive(true);
        return weatherPhenomenonFee;
    }

    public WeatherPhenomenonFeeDTO toDTO(WeatherPhenomenonFee fee) {
        WeatherPhenomenonFeeDTO dto = new WeatherPhenomenonFeeDTO();

        Vehicle vehicle = fee.getVehicle();

        dto.setId(fee.getId());
        dto.setVehicleId(vehicle.getId());
        dto.setVehicleName(vehicle.getName());
        dto.setAmount(fee.getAmount());
        dto.setForbidden(fee.isForbidden());
        dto.setActive(fee.isActive());
        dto.setCreatedAt(fee.getCreatedAt());
        dto.setDeactivatedAt(fee.getDeactivatedAt());
        dto.setPhenomenon(fee.getPhenomenon());
        return dto;
    }
}

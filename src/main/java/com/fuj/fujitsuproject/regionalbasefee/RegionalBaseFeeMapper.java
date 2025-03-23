package com.fuj.fujitsuproject.regionalbasefee;

import com.fuj.fujitsuproject.city.City;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Class that provided methods for mapping RegionalBaseFee to and from DTO.
 */
@Component
public class RegionalBaseFeeMapper {

    /**
     * Created RegionalBaseFee based on vehicle, city and amount
     * @param city provided city to create RegionalBaseFeeEntity
     * @param vehicle provided vehicle to create RegionalBaseFee
     * @param amount provided amount to create RegionalBaseFee
     * @return returns created RegionalBaseFee.
     */
    public RegionalBaseFee toRegionalBaseFee(City city, Vehicle vehicle, BigDecimal amount) {
        RegionalBaseFee regionalBaseFee = new RegionalBaseFee();
        regionalBaseFee.setCity(city);
        regionalBaseFee.setVehicle(vehicle);
        regionalBaseFee.setActive(true);
        regionalBaseFee.setAmount(amount);

        return regionalBaseFee;
    }

    /**
     * Method for converting RegionalBaseFee to DTO.
     * @param regionalBaseFee provided regional base fee to map to DTO.
     * @return returns regional base fee converted to DTO.
     */
    public RegionalBaseFeeDTO toDTO(RegionalBaseFee regionalBaseFee) {
        RegionalBaseFeeDTO dto = new RegionalBaseFeeDTO();

        City city = regionalBaseFee.getCity();
        Vehicle vehicle = regionalBaseFee.getVehicle();

        dto.setId(regionalBaseFee.getId());

        dto.setCityId(city.getId());
        dto.setCityName(city.getName());
        dto.setStationName(city.getStationName());

        dto.setVehicleId(vehicle.getId());
        dto.setVehicleName(vehicle.getName());

        dto.setAmount(regionalBaseFee.getAmount());
        dto.setActive(regionalBaseFee.isActive());
        dto.setCreatedAt(regionalBaseFee.getCreatedAt());
        dto.setDeactivatedAt(regionalBaseFee.getDeactivatedAt());

        return dto;
    }
}

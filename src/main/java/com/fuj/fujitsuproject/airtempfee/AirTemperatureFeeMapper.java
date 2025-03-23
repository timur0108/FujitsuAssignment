package com.fuj.fujitsuproject.airtempfee;

import com.fuj.fujitsuproject.vehicle.Vehicle;
import org.springframework.stereotype.Component;

/**
 * Class provided methods for converting AirTemperatureFee to and from DTO.
 */
@Component
public class AirTemperatureFeeMapper {

    /**
     * Converts AirTemperatureFeeCreateDTO to AirTemperatureFee
     * @param dto dto with provided data to create AirTemperatureFee
     * @param vehicle provided vehicle to create AirTemperatureFee
     * @return returns AirTemperatureFee converted from dto
     */
    public AirTemperatureFee toAirTemperatureFee(AirTemperatureFeeCreateDTO dto, Vehicle vehicle) {
        AirTemperatureFee airTemperatureFee = new AirTemperatureFee();
        airTemperatureFee.setMinTemperature(dto.getMinTemperature());
        airTemperatureFee.setMaxTemperature(dto.getMaxTemperature());
        airTemperatureFee.setVehicle(vehicle);
        airTemperatureFee.setActive(true);
        airTemperatureFee.setAmount(dto.getAmount());
        airTemperatureFee.setForbidden(dto.isForbidden());
        return airTemperatureFee;
    }

    /**
     * Converts AirTemperatureFee to DTO.
     * @param airTemperatureFee provided AirTemperatureFee to convert to DTO.
     * @return converted DTO.
     */
    public AirTemperatureFeeDTO toDTO(AirTemperatureFee airTemperatureFee) {
        AirTemperatureFeeDTO dto = new AirTemperatureFeeDTO();
        dto.setId(airTemperatureFee.getId());
        dto.setAmount(airTemperatureFee.getAmount());
        dto.setForbidden(airTemperatureFee.isForbidden());
        dto.setMaxTemperature(airTemperatureFee.getMaxTemperature());
        dto.setMinTemperature(airTemperatureFee.getMinTemperature());
        dto.setVehicleId(airTemperatureFee.getVehicle().getId());
        dto.setDeactivatedAt(airTemperatureFee.getDeactivatedAt());
        dto.setCreatedAt(airTemperatureFee.getCreatedAt());
        dto.setActive(airTemperatureFee.isActive());
        dto.setVehicleName(airTemperatureFee.getVehicle().getName());
        return dto;
    }
}

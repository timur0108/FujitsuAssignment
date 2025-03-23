package com.fuj.fujitsuproject.windspeedfee;

import com.fuj.fujitsuproject.vehicle.Vehicle;
import org.springframework.stereotype.Component;

/**
 * Provides methods to map WindSpeedFee to and from DTO.
 */
@Component
public class WindSpeedFeeMapper {

    public WindSpeedFee toWindSpeedFee(WindSpeedFeeCreateDTO dto, Vehicle vehicle) {

        WindSpeedFee windSpeedFee = new WindSpeedFee();
        windSpeedFee.setMinSpeed(dto.getMinSpeed());
        windSpeedFee.setMaxSpeed(dto.getMaxSpeed());
        windSpeedFee.setAmount(dto.getAmount());
        windSpeedFee.setForbidden(dto.isForbidden());
        windSpeedFee.setVehicle(vehicle);
        windSpeedFee.setActive(true);

        return windSpeedFee;
    }

    public WindSpeedFeeDTO toDTO(WindSpeedFee fee) {
        WindSpeedFeeDTO dto = new WindSpeedFeeDTO();

        Vehicle vehicle = fee.getVehicle();

        dto.setId(fee.getId());
        dto.setVehicleId(vehicle.getId());
        dto.setVehicleName(vehicle.getName());
        dto.setAmount(fee.getAmount());
        dto.setForbidden(fee.isForbidden());
        dto.setActive(fee.isActive());
        dto.setCreatedAt(fee.getCreatedAt());
        dto.setDeactivatedAt(fee.getDeactivatedAt());
        dto.setMinSpeed(fee.getMinSpeed());
        dto.setMaxSpeed(fee.getMaxSpeed());
        return dto;
    }
}

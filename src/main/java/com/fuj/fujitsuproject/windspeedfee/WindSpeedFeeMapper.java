package com.fuj.fujitsuproject.windspeedfee;

import com.fuj.fujitsuproject.vehicle.Vehicle;
import org.springframework.stereotype.Component;

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
}

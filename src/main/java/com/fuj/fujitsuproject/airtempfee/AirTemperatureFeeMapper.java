package com.fuj.fujitsuproject.airtempfee;

import com.fuj.fujitsuproject.vehicle.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class AirTemperatureFeeMapper {

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
}

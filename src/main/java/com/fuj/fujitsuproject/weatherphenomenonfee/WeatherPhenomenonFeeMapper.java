package com.fuj.fujitsuproject.weatherphenomenonfee;

import com.fuj.fujitsuproject.vehicle.Vehicle;
import org.springframework.stereotype.Component;

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
}

package com.fuj.fujitsuproject.regionalbasefee;

import com.fuj.fujitsuproject.city.City;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RegionalBaseFeeMapper {

    public RegionalBaseFee toRegionalBaseFee(City city, Vehicle vehicle, BigDecimal amount) {
        RegionalBaseFee regionalBaseFee = new RegionalBaseFee();
        regionalBaseFee.setCity(city);
        regionalBaseFee.setVehicle(vehicle);
        regionalBaseFee.setActive(true);
        regionalBaseFee.setAmount(amount);

        return regionalBaseFee;
    }
}

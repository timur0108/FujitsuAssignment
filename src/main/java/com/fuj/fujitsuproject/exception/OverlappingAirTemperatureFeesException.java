package com.fuj.fujitsuproject.exception;

import com.fuj.fujitsuproject.domain.airtempfee.AirTemperatureFee;

import java.util.List;

public class OverlappingAirTemperatureFeesException extends RuntimeException{

    public OverlappingAirTemperatureFeesException(
            List<AirTemperatureFee> overlappingAirTemperatureFees) {

        super("Couldn't add air temperature fee. Deactivate air temperature fees with " +
                "overlapping temperature ranges first. " + overlappingAirTemperatureFees.toString());
    }
}

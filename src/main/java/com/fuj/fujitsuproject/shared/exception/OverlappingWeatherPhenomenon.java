package com.fuj.fujitsuproject.shared.exception;

import com.fuj.fujitsuproject.weatherphenomenonfee.WeatherPhenomenonFee;

import java.util.List;

public class OverlappingWeatherPhenomenon extends RuntimeException{

    public OverlappingWeatherPhenomenon(String phenomenon, List<WeatherPhenomenonFee> fees) {
        super("Active weather phenomenon fee for phenomenon=" + phenomenon
        + " already exists. Deactivate it first to add new one. " + fees);
    }
}

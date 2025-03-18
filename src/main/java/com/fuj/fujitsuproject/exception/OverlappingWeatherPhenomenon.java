package com.fuj.fujitsuproject.exception;

public class OverlappingWeatherPhenomenon extends RuntimeException{

    public OverlappingWeatherPhenomenon(String phenomenon) {
        super("Active weather phenomenon fee for phenomenon=" + phenomenon
        + " already exists. Deactivate it first to add new one.");
    }
}

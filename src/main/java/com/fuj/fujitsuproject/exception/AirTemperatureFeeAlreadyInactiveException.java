package com.fuj.fujitsuproject.exception;

public class AirTemperatureFeeAlreadyInactiveException extends RuntimeException{

    public AirTemperatureFeeAlreadyInactiveException() {
        super("This air temperature fee is already inactive");
    }
}

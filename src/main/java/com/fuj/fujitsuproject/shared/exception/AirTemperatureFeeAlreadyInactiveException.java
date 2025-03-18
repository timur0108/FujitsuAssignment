package com.fuj.fujitsuproject.shared.exception;

public class AirTemperatureFeeAlreadyInactiveException extends RuntimeException{

    public AirTemperatureFeeAlreadyInactiveException() {
        super("This air temperature fee is already inactive");
    }
}

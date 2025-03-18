package com.fuj.fujitsuproject.shared.exception;

public class VehicleForbiddenException extends RuntimeException{

    public VehicleForbiddenException() {
        super("Usage of selected vehicle type is forbidden");
    }
}

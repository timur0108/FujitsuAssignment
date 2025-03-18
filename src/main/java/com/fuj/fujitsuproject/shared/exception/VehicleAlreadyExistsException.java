package com.fuj.fujitsuproject.shared.exception;

public class VehicleAlreadyExistsException extends RuntimeException{

    public VehicleAlreadyExistsException() {
        super("Such vehicle already exists.");
    }
}

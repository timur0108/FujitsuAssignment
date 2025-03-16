package com.fuj.fujitsuproject.exception;

public class VehicleAlreadyExistsException extends RuntimeException{

    public VehicleAlreadyExistsException() {
        super("Such vehicle already exists.");
    }
}

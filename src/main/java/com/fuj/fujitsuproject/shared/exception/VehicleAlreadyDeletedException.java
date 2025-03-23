package com.fuj.fujitsuproject.shared.exception;

public class VehicleAlreadyDeletedException extends RuntimeException{

    public VehicleAlreadyDeletedException(Long id) {
        super("Vehicle with id=" + id + " is already deleted.");
    }
}

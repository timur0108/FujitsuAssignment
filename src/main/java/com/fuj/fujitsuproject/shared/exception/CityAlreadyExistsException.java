package com.fuj.fujitsuproject.shared.exception;


public class CityAlreadyExistsException extends RuntimeException{

    public CityAlreadyExistsException() {
        super("City with such name already exists.");
    }
}

package com.fuj.fujitsuproject.exception;


public class CityAlreadyExistsException extends RuntimeException{

    public CityAlreadyExistsException() {
        super("City with such name already exists.");
    }
}

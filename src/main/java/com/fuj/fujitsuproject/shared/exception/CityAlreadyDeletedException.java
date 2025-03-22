package com.fuj.fujitsuproject.shared.exception;

public class CityAlreadyDeletedException extends RuntimeException{

    public CityAlreadyDeletedException(Long id) {
        super("City with id=" + id + " is already deleted");
    }
}

package com.fuj.fujitsuproject.exception;

public class RegionalBaseFeeAlreadyExistsException extends RuntimeException{

    public RegionalBaseFeeAlreadyExistsException() {
        super("Active regional base fee for such vehicle and city already exists. " +
                "Deactivate current regional base fee to add new one.");
    }
}

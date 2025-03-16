package com.fuj.fujitsuproject.exception;

public class RegionalBaseFeeAlreadyInactiveException extends RuntimeException{

    public RegionalBaseFeeAlreadyInactiveException() {
        super("This regional base fee is already inactive.");
    }
}

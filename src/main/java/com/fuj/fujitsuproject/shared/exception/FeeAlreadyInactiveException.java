package com.fuj.fujitsuproject.shared.exception;

public class FeeAlreadyInactiveException extends RuntimeException{

    public FeeAlreadyInactiveException() {
        super("This fee is already inactive.");
    }
}

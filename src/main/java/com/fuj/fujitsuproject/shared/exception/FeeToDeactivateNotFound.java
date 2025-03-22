package com.fuj.fujitsuproject.shared.exception;

public class FeeToDeactivateNotFound extends RuntimeException{

    public FeeToDeactivateNotFound(Long id) {
        super("Couldn't find fee to be deactivated with id=" + id);
    }
}

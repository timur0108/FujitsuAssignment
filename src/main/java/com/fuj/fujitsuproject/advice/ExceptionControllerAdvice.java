package com.fuj.fujitsuproject.advice;

import com.fuj.fujitsuproject.DTO.ErrorDetails;
import com.fuj.fujitsuproject.exception.VehicleForbiddenException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDetails> exceptionEntityNotFoundHandler(EntityNotFoundException e) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(VehicleForbiddenException.class)
    public ResponseEntity<ErrorDetails> exceptionVehicleForbiddenHandler(VehicleForbiddenException e) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }
}

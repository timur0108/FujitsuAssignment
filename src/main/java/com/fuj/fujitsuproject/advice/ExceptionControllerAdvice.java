package com.fuj.fujitsuproject.advice;

import com.fuj.fujitsuproject.DTO.ErrorDetails;
import com.fuj.fujitsuproject.exception.*;
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
    public ResponseEntity<ErrorDetails> exceptionVehicleForbiddenHandler(
            VehicleForbiddenException e) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(CityAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> exceptionEntityAlreadyExistsHandler(
            CityAlreadyExistsException e) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(VehicleAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> exceptionVehicleAlreadyExistsHandler(
            VehicleAlreadyExistsException e) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(RegionalBaseFeeAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> exceptionRegionalBaseFeeLAlreadyExistsHandler(
            RegionalBaseFeeAlreadyExistsException e) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(RegionalBaseFeeAlreadyInactiveException.class)
    public ResponseEntity<ErrorDetails> exceptionRegionBaseFeeAlreadyInactiveHandler(
            RegionalBaseFeeAlreadyInactiveException e) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(OverlappingAirTemperatureFeesException.class)
    public ResponseEntity<ErrorDetails> exceptionOverlappingAirTemperatureFeesHandler(
            OverlappingAirTemperatureFeesException e) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(AirTemperatureFeeAlreadyInactiveException.class)
    public ResponseEntity<ErrorDetails> exceptionAirTemperatureFeeAlreadyInactiveHandler(
            AirTemperatureFeeAlreadyInactiveException e) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(OverlappingWindSpeedFeeException.class)
    public ResponseEntity<ErrorDetails> exceptionOverlappingWindSpeedFeeHandler(
            OverlappingWindSpeedFeeException e) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(OverlappingWeatherPhenomenon.class)
    public ResponseEntity<ErrorDetails> exceptionOverlappingWeatherPhenomenonHandler(
            OverlappingWeatherPhenomenon e) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }
}

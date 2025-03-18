package com.fuj.fujitsuproject.shared.exception;

import com.fuj.fujitsuproject.windspeedfee.WindSpeedFee;

import java.util.List;

public class OverlappingWindSpeedFeeException extends RuntimeException{

    public OverlappingWindSpeedFeeException(List<WindSpeedFee> overlappingFees) {

        super("Couldn't add wind speed fee. Deactivate wind speed fees with overlapping wind speed ranges first. " +
                overlappingFees.toString());
    }
}

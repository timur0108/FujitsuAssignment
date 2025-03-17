package com.fuj.fujitsuproject.application;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliveryFeeCalculationDTO {

    private String city;
    private String vehicle;
    private LocalDateTime time;
}

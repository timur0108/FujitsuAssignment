package com.fuj.fujitsuproject.airtempfee;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AirTemperatureFeeDTO {

    private Long id;
    private Long vehicleId;
    private String vehicleName;
    private BigDecimal amount;
    private boolean forbidden;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime deactivatedAt;
    private BigDecimal minTemperature;
    private BigDecimal maxTemperature;
}

package com.fuj.fujitsuproject.windspeedfee;

import com.fuj.fujitsuproject.vehicle.Vehicle;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WindSpeedFeeDTO {

    private Long id;
    private Long vehicleId;
    private String vehicleName;
    private BigDecimal amount;
    private boolean forbidden;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime deactivatedAt;
    private BigDecimal minSpeed;
    private BigDecimal maxSpeed;
}

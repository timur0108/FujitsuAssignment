package com.fuj.fujitsuproject.regionalbasefee;

import com.fuj.fujitsuproject.city.City;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RegionalBaseFeeDTO {

    private Long id;
    private Long cityId;
    private String cityName;
    private String stationName;
    private Long vehicleId;
    private String vehicleName;
    private BigDecimal amount;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime deactivatedAt;
}

package com.fuj.fujitsuproject.airtempfee;

import com.fuj.fujitsuproject.shared.entity.VehicleAndWeatherBasedFee;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "atef")
@NoArgsConstructor
public class AirTemperatureFee extends VehicleAndWeatherBasedFee {

    @Column(name = "min_temperature", nullable = false)
    private BigDecimal minTemperature;

    @Column(name = "max_temperature", nullable = false)
    private BigDecimal maxTemperature;

    @Column(name = "amount")
    private BigDecimal amount;

}

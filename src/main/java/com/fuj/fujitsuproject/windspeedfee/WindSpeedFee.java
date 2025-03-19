package com.fuj.fujitsuproject.windspeedfee;

import com.fuj.fujitsuproject.shared.entity.VehicleAndWeatherBasedFee;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wsef")
@NoArgsConstructor
public class WindSpeedFee extends VehicleAndWeatherBasedFee {

    @Column(name = "min_speed")
    private BigDecimal minSPeed;

    @Column(name = "max_speed")
    private BigDecimal maxSPeed;

}

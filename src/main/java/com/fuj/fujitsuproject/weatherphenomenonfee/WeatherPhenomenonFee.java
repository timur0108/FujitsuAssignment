package com.fuj.fujitsuproject.weatherphenomenonfee;

import com.fuj.fujitsuproject.shared.entity.VehicleAndWeatherBasedFee;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wpef")
@NoArgsConstructor
public class WeatherPhenomenonFee extends VehicleAndWeatherBasedFee {

    @Column(name = "phenomenon", nullable = false)
    private String phenomenon;

}

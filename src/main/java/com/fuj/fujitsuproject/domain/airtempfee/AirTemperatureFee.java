package com.fuj.fujitsuproject.domain.airtempfee;

import com.fuj.fujitsuproject.domain.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "atef")
@NoArgsConstructor
public class AirTemperatureFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "min_temperature", nullable = false)
    private BigDecimal minTemperature;

    @Column(name = "max_temperature", nullable = false)
    private BigDecimal maxTemperature;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "forbidden", nullable = false)
    private boolean forbidden;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }
}

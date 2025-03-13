package com.fuj.fujitsuproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wpef")
@NoArgsConstructor
public class WeatherPhenomenonFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "phenomenon", nullable = false)
    private String phenomenon;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "forbidden", nullable = false)
    private boolean forbidden;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}

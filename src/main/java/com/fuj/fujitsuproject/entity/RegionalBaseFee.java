package com.fuj.fujitsuproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rbf")
@NoArgsConstructor
public class RegionalBaseFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal amount;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

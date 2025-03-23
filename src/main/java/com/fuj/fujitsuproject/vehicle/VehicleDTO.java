package com.fuj.fujitsuproject.vehicle;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleDTO {

    private Long id;
    private String name;
    private boolean deleted;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
}

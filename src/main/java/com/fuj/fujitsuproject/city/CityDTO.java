package com.fuj.fujitsuproject.city;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CityDTO {

    private Long id;
    private String name;
    private String stationName;
    private boolean deleted;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
}

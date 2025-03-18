package com.fuj.fujitsuproject.city;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CityCreateDTO {

    @NotBlank(message = "Name must not be empty")
    @NotNull(message = "Name must not be null")
    private String name;

    @NotBlank(message = "Station name must not be empty")
    @NotNull(message = "Station name must not be null")
    private String stationName;
}

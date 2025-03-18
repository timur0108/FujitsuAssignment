package com.fuj.fujitsuproject.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleCreateDTO {

    @NotBlank(message = "Name must not be empty")
    @NotNull(message = "Name must not be null")
    private String name;
}

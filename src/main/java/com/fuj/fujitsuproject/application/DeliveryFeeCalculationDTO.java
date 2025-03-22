package com.fuj.fujitsuproject.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliveryFeeCalculationDTO {

    @NotNull(message = "City can't be null")
    @NotBlank(message = "City can't be empty")
    private String city;

    @NotNull(message = "vehicle can't be null")
    @NotEmpty(message = "vehicle can't be empty")
    private String vehicle;

    private LocalDateTime time;
}

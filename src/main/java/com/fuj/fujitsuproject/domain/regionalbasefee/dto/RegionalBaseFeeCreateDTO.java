package com.fuj.fujitsuproject.domain.regionalbasefee.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RegionalBaseFeeCreateDTO {

    @NotNull(message = "City id can't be null")
    private Long cityId;

    @NotNull(message = "Vehicle id can't be null")
    private Long vehicleId;

    @NotNull(message = "Amount can't be null")
    private BigDecimal amount;
}

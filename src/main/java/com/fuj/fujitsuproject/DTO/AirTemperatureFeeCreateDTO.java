package com.fuj.fujitsuproject.DTO;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AirTemperatureFeeCreateDTO {

    @NotNull(message = "vehicle id can't be null")
    private Long vehicleId;

    @NotNull(message = "minimum temperature can't be null")
    private BigDecimal minTemperature;

    @NotNull(message = "maximum temperature can't be null")
    private BigDecimal maxTemperature;

    private BigDecimal amount;

    @NotNull(message = "forbidden can't be null")
    private boolean forbidden;

    @AssertTrue(message = "Amount must not be null when forbidden is false")
    public boolean isAmountValid() {
        return Boolean.TRUE.equals(forbidden) || amount != null;
    }
}

package com.fuj.fujitsuproject.domain.windspeedfee.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WindSpeedFeeCreateDTO {

    @NotNull(message = "vehicle id can't be null")
    private Long vehicleId;

    @NotNull(message = "minimum speed can't be null")
    private BigDecimal minSpeed;

    @NotNull(message = "maximum speed can't be null")
    private BigDecimal maxSpeed;

    private BigDecimal amount;

    @NotNull(message = "forbidden can't be null")
    private boolean forbidden;

    @AssertTrue(message = "Amount must not be null when forbidden is false")
    public boolean isAmountValid() {
        return Boolean.TRUE.equals(forbidden) || amount != null;
    }
}

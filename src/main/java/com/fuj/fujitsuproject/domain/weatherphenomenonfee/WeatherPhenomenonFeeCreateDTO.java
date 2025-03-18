package com.fuj.fujitsuproject.domain.weatherphenomenonfee;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WeatherPhenomenonFeeCreateDTO {

    @NotNull(message = "vehicle id can't be null")
    private Long vehicleId;

    @NotNull(message = "phenomenon can't be null")

    @NotBlank(message = "phenomenon can't be empty")
    @NotNull(message = "phenomenon can't be null")
    private String phenomenon;

    private BigDecimal amount;

    @NotNull(message = "forbidden can't be null")
    private boolean forbidden;

    @AssertTrue(message = "Amount must not be null when forbidden is false")
    public boolean isAmountValid() {
        return Boolean.TRUE.equals(forbidden) || amount != null;
    }
}

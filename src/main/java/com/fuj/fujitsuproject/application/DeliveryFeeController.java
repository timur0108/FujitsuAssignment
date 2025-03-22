package com.fuj.fujitsuproject.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/fee/calculate")
public class DeliveryFeeController {

    private final DeliveryFeeCalculationService deliveryFeeCalculationService;

    /**
     * Controller that accepts dto containing city and vehicle and time if provided
     * to calculate delivery fee.
     * @param deliveryFeeCalculationDTO DTO containing all needed information to calculate
     *                                  delivery fee.
     * @return returns ResponseEntity containing BigDecimal of calculated delivery fee.
     */
    @PostMapping
    public ResponseEntity<BigDecimal> calculateDeliveryFee(
            @RequestBody DeliveryFeeCalculationDTO deliveryFeeCalculationDTO) {

        log.info("Arrived POST request to calculate delivery fee.");
        return ResponseEntity.ok(
                deliveryFeeCalculationService.calculateDeliveryFee(deliveryFeeCalculationDTO)
        );
    }
}

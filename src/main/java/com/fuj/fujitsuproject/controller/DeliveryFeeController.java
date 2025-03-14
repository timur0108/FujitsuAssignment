package com.fuj.fujitsuproject.controller;

import com.fuj.fujitsuproject.DTO.DeliveryFeeCalculationDTO;
import com.fuj.fujitsuproject.service.FeeCalculationService;
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

    private final FeeCalculationService feeCalculationService;

    @PostMapping
    public ResponseEntity<BigDecimal> calculateDeliveryFee(
            @RequestBody DeliveryFeeCalculationDTO deliveryFeeCalculationDTO) {

        log.info("Arrived POST request to calculate delivery fee.");
        return ResponseEntity.ok(
                feeCalculationService.calculateDeliveryFee(deliveryFeeCalculationDTO)
        );
    }
}

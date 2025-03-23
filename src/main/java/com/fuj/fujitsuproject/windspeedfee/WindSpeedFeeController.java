package com.fuj.fujitsuproject.windspeedfee;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/wsef")
public class WindSpeedFeeController {

    private final WindSpeedFeeService windSpeedFeeService;

    /**
     * Handles GET request. Searches for all wind speed fees from database.
     * @param activeOnly If activeOnly is true then returns only those fees
     *                   that are currently active. Otherwise, searches for all fees.
     * @return ResponseEntity containing all found wind speed fees as DTOs.
     */
    @GetMapping("/all")
    public ResponseEntity<List<WindSpeedFeeDTO>> getAllWindSpeedFees(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly
    ) {
        return ResponseEntity
                .ok()
                .body(windSpeedFeeService.findAllWindSpeedFees(activeOnly));
    }

    /**
     * Handles POST request. Creates new wind speed fee.
     * @param windSpeedFeeCreateDTO DTO that contains all the needed data to
     *                              create new wind speed fee.
     * @return ResponseEntity containing newly crated wind speed fee.
     */
    @PostMapping
    public ResponseEntity<WindSpeedFeeDTO> addWindSpeedFee(
            @RequestBody @Valid WindSpeedFeeCreateDTO windSpeedFeeCreateDTO) {

        return new ResponseEntity<>(windSpeedFeeService
                .createWindSpeedFee(windSpeedFeeCreateDTO), HttpStatus.CREATED);
    }

    /**
     * Handles PATCH request. Deactivate fee by provided id.
     * @param id id to deactivate fee by.
     * @return empty ResponseEntity.
     */
    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateWindSPeedFee(@PathVariable Long id) {
        windSpeedFeeService.deactivateFee(id);
        return ResponseEntity.ok().build();
    }
}

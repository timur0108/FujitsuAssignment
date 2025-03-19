package com.fuj.fujitsuproject.windspeedfee;

import com.fuj.fujitsuproject.windspeedfee.dto.WindSpeedFeeCreateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/wpf")
public class WindSpeedFeeController {

    private final WindSpeedFeeService windSpeedFeeService;

    @GetMapping("/all")
    public ResponseEntity<List<WindSpeedFee>> getAllWindSpeedFees() {
        return ResponseEntity
                .ok()
                .body(windSpeedFeeService.findAllWindSpeedFees());
    }

    @PostMapping
    public ResponseEntity<WindSpeedFee> addWindSpeedFee(
            @RequestBody @Valid WindSpeedFeeCreateDTO windSpeedFeeCreateDTO) {

        return new ResponseEntity<>(windSpeedFeeService
                .createWindSpeedFee(windSpeedFeeCreateDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateWindSPeedFee(@PathVariable Long id) {
        windSpeedFeeService.deactivateFee(id);
        return ResponseEntity.ok().build();
    }
}

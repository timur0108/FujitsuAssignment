package com.fuj.fujitsuproject.controller;

import com.fuj.fujitsuproject.DTO.AirTemperatureFeeCreateDTO;
import com.fuj.fujitsuproject.entity.AirTemperatureFee;
import com.fuj.fujitsuproject.service.AirTemperatureFeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/atef")
public class AirTemperatureFeeController {

    private final AirTemperatureFeeService airTemperatureFeeService;

    @GetMapping("/all")
    public ResponseEntity<List<AirTemperatureFee>> getAllAirTemperatureFees() {
        return ResponseEntity
                .ok()
                .body(airTemperatureFeeService.findALlAirTemperatureFees());
    }

    @PostMapping
    public ResponseEntity<AirTemperatureFee> addAirTemperatureFee(
            @RequestBody @Valid AirTemperatureFeeCreateDTO airTemperatureFeeCreateDTO) {

        return new ResponseEntity<>(
                airTemperatureFeeService.createAirTemperatureFee(airTemperatureFeeCreateDTO),
                HttpStatus.CREATED);
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateAirTemperatureFee(@PathVariable Long id) {
        airTemperatureFeeService.deactivateAirTemperatureFee(id);
        return ResponseEntity.ok().build();
    }
}

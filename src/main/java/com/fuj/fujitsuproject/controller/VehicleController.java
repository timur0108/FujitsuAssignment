package com.fuj.fujitsuproject.controller;

import com.fuj.fujitsuproject.DTO.VehicleCreateDTO;
import com.fuj.fujitsuproject.entity.City;
import com.fuj.fujitsuproject.entity.Vehicle;
import com.fuj.fujitsuproject.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping("/all")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {

        return ResponseEntity
                .ok(vehicleService.getAllVehicles());
    }

    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody @Valid VehicleCreateDTO vehicleCreateDTO) {

        return new ResponseEntity<>(vehicleService.addVehicle(vehicleCreateDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id,
                                                 @RequestBody @Valid VehicleCreateDTO vehicleCreateDTO) {

        return ResponseEntity
                .ok()
                .body(vehicleService.updateVehicle(id, vehicleCreateDTO));
    }
}

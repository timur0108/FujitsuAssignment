package com.fuj.fujitsuproject.vehicle;

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

    /**
     * Handles GET request and returns all the vehicles from database.
     * @param notDeletedOnly If notDeletedOnly is set ti true then returns only those
     *                       vehicles that are not currently deleted.
     * @return List of found vehicles as DTOs.
     */
    @GetMapping("/all")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles(
            @RequestParam(required = false, defaultValue = "false") boolean notDeletedOnly) {
        return ResponseEntity
                .ok(vehicleService.getAllVehicles(notDeletedOnly));
    }

    /**
     * Handles POST request. Creates new vehicle and saves it to database.
     * @param vehicleCreateDTO
     * @return ResponseEntity containing newly created vehicle as DTO with HTTP status CREATED.
     */
    @PostMapping
    public ResponseEntity<VehicleDTO> addVehicle(@RequestBody @Valid VehicleCreateDTO vehicleCreateDTO) {

        return new ResponseEntity<>(vehicleService.addVehicle(vehicleCreateDTO), HttpStatus.CREATED);
    }

    /**
     * Handles PUT request. Updates vehicle information.
     * @param id id of vehicle that needs to be updated.
     * @param vehicleCreateDTO DTO containing all the needed information to update vehicle.
     * @returnr updated vehicle as DTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Long id,
                                                 @RequestBody @Valid VehicleCreateDTO vehicleCreateDTO) {
        return ResponseEntity
                .ok()
                .body(vehicleService.updateVehicle(id, vehicleCreateDTO));
    }

    /**
     * Handles DELETE request. Instead of actually deleting vehicle just sets deleted=true
     * to be able to calculate delivery fee for the provided time when this vehicle wasn't deleted.
     * @param id id to delete vehicle by.
     * @return Empty ResponseEntity.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

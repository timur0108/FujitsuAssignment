package com.fuj.fujitsuproject.airtempfee;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/atef")
public class AirTemperatureFeeController {

    private final AirTemperatureFeeService airTemperatureFeeService;

    /**
     * Controller for returning list of all air temperature fees.
     * @param activeOnly If activeOnly is true then returns only those fees that are
     *                   currently active.
     * @return ResponseEntity containing list of returned air temperature fees.
     */
    @GetMapping("/all")
    public ResponseEntity<List<AirTemperatureFee>> getAllAirTemperatureFees(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly
    ) {
        return ResponseEntity
                .ok()
                .body(airTemperatureFeeService.findAllFees(activeOnly));
    }

    /**
     * Controller for creating new air temperature fee.
     * @param airTemperatureFeeCreateDTO DTO containing all needed data for
     *                                   creating new air temperature fee.
     * @return returns ReponseEntity containing newly created air temperature fee.
     */
    @PostMapping
    public ResponseEntity<AirTemperatureFee> addAirTemperatureFee(
            @RequestBody @Valid AirTemperatureFeeCreateDTO airTemperatureFeeCreateDTO) {

        return new ResponseEntity<>(
                airTemperatureFeeService.createAirTemperatureFee(airTemperatureFeeCreateDTO),
                HttpStatus.CREATED);
    }

    /**
     * Controller that deactivated fee with provided id.
     * @param id id of air temperature fee that needs to be deactivated.
     * @return Empty ResponseEntity.
     */
    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateAirTemperatureFee(@PathVariable Long id) {
        airTemperatureFeeService.deactivateFee(id);
        return ResponseEntity.ok().build();
    }
}

package com.fuj.fujitsuproject.weatherphenomenonfee;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/wpef")
public class WeatherPhenomenonFeeController {

    private final WeatherPhenomenonFeeService weatherPhenomenonFeeService;

    /**
     * Handles GET request. Finds all weather phenomenon fees from database.
     * @param activeOnly If activeOnly is true then finds only those fees that are
     *                   currently active.
     * @return returns ResponseEntity containing found weather phenomenon fees as DTOs.
     */
    @GetMapping("/all")
    public ResponseEntity<List<WeatherPhenomenonFeeDTO>> getAllWeatherPhenomenonFees(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly
    ) {
        return ResponseEntity
                .ok()
                .body(weatherPhenomenonFeeService.findALlWeatherPhenomenonFees(activeOnly));
    }

    /**
     * Handles PATCH request. Deactivates selected fee.
     * @param id id to search for fee to be deactivated by.
     * @return returns empty ResponseEntity.
     */
    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateWeatherPhenomenonFee(@PathVariable Long id) {
        weatherPhenomenonFeeService.deactivateFee(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Handles POST request. Creates new weather phenomenon fee.
     * @param weatherPhenomenonFeeCreateDTO DTO containing all the needed data to
     *                                      create new weather phenomenon fee.
     * @return ResponseEntity containing newly created weather phenomenon fee as DTO.
     */
    @PostMapping
    public ResponseEntity<WeatherPhenomenonFeeDTO> createWeatherPhenomenonFee(
            @RequestBody @Valid WeatherPhenomenonFeeCreateDTO weatherPhenomenonFeeCreateDTO
    ) {

        return new ResponseEntity<>(
                weatherPhenomenonFeeService
                        .createWeatherPhenomenonFee(weatherPhenomenonFeeCreateDTO),
                HttpStatus.CREATED);
    }
}

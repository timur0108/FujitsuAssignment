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

    @GetMapping("/all")
    public ResponseEntity<List<WeatherPhenomenonFee>> getAllWeatherPhenomenonFees() {
        return ResponseEntity
                .ok()
                .body(weatherPhenomenonFeeService.findAllFees());
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateWeatherPhenomenonFee(@PathVariable Long id) {
        weatherPhenomenonFeeService.deactivateFee(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<WeatherPhenomenonFee> createWeatherPhenomenonFee(
            @RequestBody @Valid WeatherPhenomenonFeeCreateDTO weatherPhenomenonFeeCreateDTO
    ) {

        return new ResponseEntity<>(
                weatherPhenomenonFeeService
                        .createWeatherPhenomenonFee(weatherPhenomenonFeeCreateDTO),
                HttpStatus.CREATED);
    }
}

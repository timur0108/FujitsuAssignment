package com.fuj.fujitsuproject.city;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/city")
public class CityController {

    private final CityService cityService;

    @GetMapping("/all")
    public ResponseEntity<List<City>> getAllCities() {

        return ResponseEntity.ok(
                cityService.getAllCities()
        );
    }

    @PostMapping
    public ResponseEntity<City> addCity(@RequestBody @Valid CityCreateDTO cityCreateDTO) {

        return new ResponseEntity<>(cityService.addCity(cityCreateDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCityById(@PathVariable Long id) {

        cityService.deleteCityById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id,
                                           @RequestBody @Valid CityCreateDTO cityCreateDTO) {

        return ResponseEntity
                .ok()
                .body(cityService.updateCityById(id, cityCreateDTO));
    }
}

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

    /**
     * Returns list of all cities stored in database as DTOs.
     * @param notDeletedOnly if notDeletedOnly then returns list of those cities
     *                       that are not deleted.
     * @return returns ResponseEntity containing list of found cities as DTOs.
     */
    @GetMapping("/all")
    public ResponseEntity<List<CityDTO>> getAllCities(
            @RequestParam(required = false, defaultValue = "false") boolean notDeletedOnly
    ) {
        return ResponseEntity.ok(
                cityService.getAllCities(notDeletedOnly)
        );
    }

    /**
     * Creates new city entity based on provided cityCreateDTO
     * @param cityCreateDTO DTO containing all needed data to create new city.
     * @return returns ResponseEntity containing newly created city as DTO.
     */
    @PostMapping
    public ResponseEntity<CityDTO> addCity(@RequestBody @Valid CityCreateDTO cityCreateDTO) {

        return new ResponseEntity<>(cityService.addCity(cityCreateDTO), HttpStatus.CREATED);
    }

    /**
     * Deletes city by id. Instead of actually deleting city it just soft deletes city by
     * setting deleted=true, because city shouldn't be actually delete to be able
     * to calculate delivery fee at the specific time when that city wasn't yet deleted.
     * @param id id to delete city by.
     * @return returns empty ResponseEntity.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCityById(@PathVariable Long id) {
        cityService.deleteCityById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates city by changing name and station name.
     * @param id id of city to be updated.
     * @param cityCreateDTO dto containing data to update city.
     * @return returns updated City as DTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CityDTO> updateCity(@PathVariable Long id,
                                           @RequestBody @Valid CityCreateDTO cityCreateDTO) {

        return ResponseEntity
                .ok()
                .body(cityService.updateCityById(id, cityCreateDTO));
    }
}

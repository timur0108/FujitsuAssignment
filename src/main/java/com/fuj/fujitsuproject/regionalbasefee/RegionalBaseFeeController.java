package com.fuj.fujitsuproject.regionalbasefee;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/rbf")
public class RegionalBaseFeeController {

    private final RegionalBaseFeeService regionalBaseFeeService;

    /**
     * /**
     * Retrieves all regional base fees by calling the service layer.
     * @param activeOnly if activeOnly equals true retrieves only those fees
     *                   that are currently active.
     * @return ResponseEntity containing all found regional base fees.
     */
    @GetMapping("/all")
    public ResponseEntity<List<RegionalBaseFeeDTO>> getAllRegionalBaseFees(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly
    ) {
        return ResponseEntity
                .ok()
                .body(regionalBaseFeeService.findALlRegionalBaseFees(activeOnly));
    }

    /**
     * Handles POST request to add a new regional base fee.
     * Accepts a request body containing the data for a new regional base fee and
     * calls service layer to process this data. If regional base fee with such combination
     * of city and vehicle already exists, then deactivates already existing
     * regional base fee and creates new active one.
     * @param regionalBaseFeeCreateDTO the data transfer object containing the details
     *                                 for the new regional base fee.
     * @return a ResponseEntity containing the newly created regional base fee
     * with an HTTP status of CREATED.
     */
    @PostMapping
    public ResponseEntity<RegionalBaseFee> addRegionalBaseFee(
            @RequestBody @Valid RegionalBaseFeeCreateDTO regionalBaseFeeCreateDTO) {

        return new ResponseEntity<>(regionalBaseFeeService.addRegionalBaseFee(regionalBaseFeeCreateDTO), HttpStatus.CREATED);
    }

}

package com.fuj.fujitsuproject.controller;

import com.fuj.fujitsuproject.DTO.RegionalBaseFeeCreateDTO;
import com.fuj.fujitsuproject.entity.RegionalBaseFee;
import com.fuj.fujitsuproject.service.RegionalBaseFeeService;
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

    @GetMapping("/all")
    public ResponseEntity<List<RegionalBaseFee>> getAllRegionalBaseFees() {

        return ResponseEntity
                .ok()
                .body(regionalBaseFeeService.findALlRegionalBaseFees());
    }

    @PostMapping
    public ResponseEntity<RegionalBaseFee> addRegionalBaseFee(
            @RequestBody @Valid RegionalBaseFeeCreateDTO regionalBaseFeeCreateDTO) {

        return new ResponseEntity<>(regionalBaseFeeService.addRegionalBaseFee(regionalBaseFeeCreateDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateRegionalBaseFee(@PathVariable Long id) {

        regionalBaseFeeService.deactivateRegionalBaseFee(id);

        return ResponseEntity.ok().build();
    }
}

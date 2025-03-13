package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.entity.Vehicle;
import com.fuj.fujitsuproject.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public Vehicle findVehicleByName(String vehicleName) {
        return vehicleRepository
                .findByNameEquals(vehicleName.toLowerCase())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find vehicle with name=" + vehicleName
                ));
    }
}

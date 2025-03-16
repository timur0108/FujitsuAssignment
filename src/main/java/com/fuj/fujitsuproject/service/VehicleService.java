package com.fuj.fujitsuproject.service;

import com.fuj.fujitsuproject.DTO.VehicleCreateDTO;
import com.fuj.fujitsuproject.entity.Vehicle;
import com.fuj.fujitsuproject.exception.VehicleAlreadyExistsException;
import com.fuj.fujitsuproject.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public Vehicle findVehicleById(Long id) {
        return vehicleRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find vehicle with id=" + id));
    }

    public Vehicle findVehicleByName(String vehicleName) {
        return vehicleRepository
                .findByNameEqualsIgnoreCase(vehicleName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find vehicle with name=" + vehicleName
                ));
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle addVehicle(VehicleCreateDTO vehicleCreateDTO) {
        Optional<Vehicle> existingVehicle = vehicleRepository
                .findByNameEqualsIgnoreCase(vehicleCreateDTO.getName());

        if (existingVehicle.isPresent()) throw new VehicleAlreadyExistsException();

        Vehicle vehicle = new Vehicle();
        vehicle.setName(vehicle.getName());

        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Long id, VehicleCreateDTO vehicleCreateDTO) {

        Vehicle vehicle = vehicleRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find vehicle to delete"));

        vehicle.setName(vehicle.getName());

        return vehicleRepository.save(vehicle);
    }
}

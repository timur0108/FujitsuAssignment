package com.fuj.fujitsuproject.vehicle;

import com.fuj.fujitsuproject.regionalbasefee.RegionalBaseFee;
import com.fuj.fujitsuproject.regionalbasefee.RegionalBaseFeeService;
import com.fuj.fujitsuproject.shared.exception.FeeAlreadyInactiveException;
import com.fuj.fujitsuproject.shared.exception.FeeToDeactivateNotFound;
import com.fuj.fujitsuproject.shared.exception.VehicleAlreadyExistsException;
import com.fuj.fujitsuproject.shared.service.VehicleAndWeatherBasedFeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private List<VehicleAndWeatherBasedFeeService> services;
    private RegionalBaseFeeService regionalBaseFeeService;

    @Autowired
    public void setServices(@Lazy List<VehicleAndWeatherBasedFeeService> services,
                            @Lazy RegionalBaseFeeService regionalBaseFeeService) {
        this.services = services;
        this.regionalBaseFeeService = regionalBaseFeeService;
    }

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

    public Vehicle findVehicleByNameAndTime(String name, Optional<LocalDateTime> time) {
        if (time.isPresent()) {
            return vehicleRepository.findByNameAndTime(name, time.get())
                    .orElseThrow(() -> new EntityNotFoundException("Couldn't find vehicle"));
        }
        return vehicleRepository.findByNameEqualsIgnoreCaseAndDeletedFalse(name)
                .orElseThrow(() -> new EntityNotFoundException("Couldn't find vehicle"));

    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle addVehicle(VehicleCreateDTO vehicleCreateDTO) {
        Optional<Vehicle> existingVehicle = vehicleRepository
                .findByNameEqualsIgnoreCaseAndDeletedFalse(vehicleCreateDTO.getName());

        if (existingVehicle.isPresent()) throw new VehicleAlreadyExistsException();

        Vehicle vehicle = new Vehicle();
        vehicle.setName(vehicleCreateDTO.getName());
        vehicle.setDeleted(false);

        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public void deleteVehicle(Long id) {
        //должен ещё деактивироваться regional base fee
        regionalBaseFeeService.deactivateRegionalBaseFeeByVehicle(id);
        services.stream().forEach(service -> service.deactivateByVehicleId(id));
        Vehicle vehicle = findVehicleById(id);
        vehicle.setDeleted(true);
        vehicle.setDeletedAt(LocalDateTime.now());
        vehicleRepository.save(vehicle);
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

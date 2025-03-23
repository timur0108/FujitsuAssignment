package com.fuj.fujitsuproject.vehicle;

import com.fuj.fujitsuproject.regionalbasefee.RegionalBaseFee;
import com.fuj.fujitsuproject.regionalbasefee.RegionalBaseFeeService;
import com.fuj.fujitsuproject.shared.exception.FeeAlreadyInactiveException;
import com.fuj.fujitsuproject.shared.exception.FeeToDeactivateNotFound;
import com.fuj.fujitsuproject.shared.exception.VehicleAlreadyDeletedException;
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
    private final VehicleMapper mapper;
    private List<VehicleAndWeatherBasedFeeService> services;
    private RegionalBaseFeeService regionalBaseFeeService;

    @Autowired
    public void setServices(@Lazy List<VehicleAndWeatherBasedFeeService> services,
                            @Lazy RegionalBaseFeeService regionalBaseFeeService) {
        this.services = services;
        this.regionalBaseFeeService = regionalBaseFeeService;
    }

    /**
     * Method for finding vehicle by id.
     * @param id id to search for vehicle by.
     * @return returns found vehicle. If not vehicle is found then exception is thrown.
     */
    public Vehicle findVehicleById(Long id) {
        return vehicleRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find vehicle with id=" + id));
    }

    /**
     * Finds vehicle by name and time if provided.
     * @param name name to search for vehicle by.
     * @param time If time is provided then searches for vehicle that existed at the time.
     * @return returns found Vehicle. Throws exception if no vehicle is found.
     */
    public Vehicle findVehicleByNameAndTime(String name, Optional<LocalDateTime> time) {
        if (time.isPresent()) {
            return vehicleRepository.findByNameAndTime(name, time.get())
                    .orElseThrow(() -> new EntityNotFoundException("Couldn't find vehicle"));
        }
        return vehicleRepository.findByNameEqualsIgnoreCaseAndDeletedFalse(name)
                .orElseThrow(() -> new EntityNotFoundException("Couldn't find vehicle"));

    }

    /**
     * Method for finding all vehicles.
     * @param notDeletedOnly If notDeletedOnly equals true then returns only
     *                       those vehicles that are currently not deleted.
     * @return returns List of found vehicles as DTOs.
     */
    public List<VehicleDTO> getAllVehicles(boolean notDeletedOnly) {
        List<Vehicle> vehicles;
        if (notDeletedOnly) vehicles = vehicleRepository.findAllByDeletedFalse();
        else vehicles = vehicleRepository.findAll();
        return vehicles
                .stream()
                .map(fee -> mapper.toDTO(fee))
                .toList();
    }

    /**
     * Method for adding new vehicle to database.
     * @param vehicleCreateDTO provided dto with all the needed information for creating a
     *                         new vehicle.
     * @return returns newly created vehicle as DTO. If vehicle with such name already exists
     * and it is not deleted then exception is thrown.
     */
    public VehicleDTO addVehicle(VehicleCreateDTO vehicleCreateDTO) {
        Optional<Vehicle> existingVehicle = vehicleRepository
                .findByNameEqualsIgnoreCaseAndDeletedFalse(vehicleCreateDTO.getName());

        if (existingVehicle.isPresent()) throw new VehicleAlreadyExistsException();

        Vehicle vehicle = mapper.toVehicle(vehicleCreateDTO);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return mapper.toDTO(savedVehicle);
    }

    /**
     * Method for deleting vehicle. It doesn't actually delete vehicle but sets
     * deleted=true to be able to calculate delivery fee for vehicle that existed at
     * the provided time. After marking vehicle as deleted deactivates all
     * regional base fees and vehicle and weather based fees that are connected to that vehicle.
     * If vehicle is already deleted throws exception.
     * @param id id to delete vehicle by.
     */
    @Transactional
    public void deleteVehicle(Long id) {
        Vehicle vehicle = findVehicleById(id);
        if (vehicle.isDeleted()) throw new VehicleAlreadyDeletedException(id);
        vehicle.setDeleted(true);
        vehicle.setDeletedAt(LocalDateTime.now());
        vehicleRepository.save(vehicle);
        regionalBaseFeeService.deactivateRegionalBaseFeeByVehicleId(id);
        services.stream().forEach(service -> service.deactivateByVehicleId(id));
    }

    /**
     * Method for updating vehicle.
     * @param id id to search vehicle by to update.
     * @param vehicleCreateDTO DTO with all the needed information to update vehicle.
     * @return returns updated vehicle as DTO.
     */
    public VehicleDTO updateVehicle(Long id, VehicleCreateDTO vehicleCreateDTO) {
        Vehicle vehicle = findVehicleById(id);
        vehicle.setName(vehicleCreateDTO.getName());
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return mapper.toDTO(updatedVehicle);
    }
}

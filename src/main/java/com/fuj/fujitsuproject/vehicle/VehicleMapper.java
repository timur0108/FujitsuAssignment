package com.fuj.fujitsuproject.vehicle;

import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.stereotype.Component;

/**
 * Class for mapping Vehicle to and from DTO.
 */
@Component
public class VehicleMapper {

    private final RestTemplateAutoConfiguration restTemplateAutoConfiguration;

    public VehicleMapper(RestTemplateAutoConfiguration restTemplateAutoConfiguration) {
        this.restTemplateAutoConfiguration = restTemplateAutoConfiguration;
    }

    /**
     * Maps vehicle to vehicleDTO
     * @param vehicle provided vehicle to map to DTO.
     * @return returns DTO that was mapped from Vehicle.
     */
    public VehicleDTO toDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setName(vehicle.getName());
        dto.setCreatedAt(vehicle.getCreatedAt());
        dto.setDeletedAt(vehicle.getDeletedAt());
        dto.setDeleted(vehicle.isDeleted());
        return dto;
    }

    /**
     * Maps VehicleDTO to Vehicle.
     * @param dto provided DTO to map to Vehicle from.
     * @return returns Vehicle mapped from DTO.
     */
    public Vehicle toVehicle(VehicleCreateDTO dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setName(dto.getName());
        vehicle.setDeleted(false);
        return vehicle;
    }
}

package com.fuj.fujitsuproject.regionalbasefee;

import com.fuj.fujitsuproject.regionalbasefee.dto.RegionalBaseFeeCreateDTO;
import com.fuj.fujitsuproject.city.City;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.shared.exception.RegionalBaseFeeAlreadyExistsException;
import com.fuj.fujitsuproject.shared.exception.RegionalBaseFeeAlreadyInactiveException;
import com.fuj.fujitsuproject.city.CityService;
import com.fuj.fujitsuproject.vehicle.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegionalBaseFeeService {

    private final RegionalBaseFeeRepository regionalBaseFeeRepository;
    private final CityService cityService;
    private final VehicleService vehicleService;

    private RegionalBaseFee findRbfByVehicleAndCityAndTime(Vehicle vehicle, City city, LocalDateTime time) {
        return regionalBaseFeeRepository
                .findRbfForParticularTimeByCityIdAndVehicleId(city.getId(), vehicle.getId(), time)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find regional base fee for vehicle=" + vehicle + ", city=" + city +
                        ", time=" + time));
    }

    private RegionalBaseFee findLatestActiveRbfByVehicleAndCity(Vehicle vehicle, City city) {
        return regionalBaseFeeRepository
                .findLatestActiveRbfByCityIdAndVehicleId(vehicle.getId(), city.getId())
                .orElseThrow(() -> new EntityNotFoundException("" +
                        "Couldn't find latest active regional base fee for vehicle" +
                        "=" + vehicle + ", city=" + city));
    }

    public BigDecimal calculateFeeForVehicleAndCity(Vehicle vehicle, City city, Optional<LocalDateTime> time) {

        RegionalBaseFee regionalBaseFee;

        if (time.isPresent()) regionalBaseFee = findRbfByVehicleAndCityAndTime(vehicle, city, time.get());
        else regionalBaseFee = findLatestActiveRbfByVehicleAndCity(vehicle, city);

        log.info("regional base fee=" + regionalBaseFee.getAmount());
        return regionalBaseFee.getAmount();
    }

    public List<RegionalBaseFee> findALlRegionalBaseFees() {
        return regionalBaseFeeRepository.findAll();
    }

    public RegionalBaseFee addRegionalBaseFee(RegionalBaseFeeCreateDTO regionalBaseFeeCreateDTO) {

        City city = cityService.findCityById(regionalBaseFeeCreateDTO.getCityId());
        Vehicle vehicle = vehicleService.findVehicleById(regionalBaseFeeCreateDTO.getVehicleId());

        Optional<RegionalBaseFee> existingRegionalBaseFee = regionalBaseFeeRepository
                .findByVehicleAndCityAndActiveTrue(vehicle, city);

        if (existingRegionalBaseFee.isPresent()) throw new RegionalBaseFeeAlreadyExistsException();

        RegionalBaseFee regionalBaseFee = new RegionalBaseFee();
        regionalBaseFee.setCity(city);
        regionalBaseFee.setVehicle(vehicle);
        regionalBaseFee.setActive(true);
        regionalBaseFee.setAmount(regionalBaseFeeCreateDTO.getAmount());

        return regionalBaseFeeRepository.save(regionalBaseFee);
    }

    public void deactivateRegionalBaseFee(Long id) {

        RegionalBaseFee regionalBaseFee = regionalBaseFeeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find regional base fee by id=" + id));

        if (!regionalBaseFee.isActive()) throw new RegionalBaseFeeAlreadyInactiveException();

        regionalBaseFee.setActive(false);
        regionalBaseFee.setDeactivatedAt(LocalDateTime.now());

        regionalBaseFeeRepository.save(regionalBaseFee);
    }
}

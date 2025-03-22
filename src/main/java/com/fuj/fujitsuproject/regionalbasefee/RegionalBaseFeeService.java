package com.fuj.fujitsuproject.regionalbasefee;

import com.fuj.fujitsuproject.city.City;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.city.CityService;
import com.fuj.fujitsuproject.vehicle.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RegionalBaseFeeMapper mapper;

    private RegionalBaseFee findRbfByVehicleAndCityAndTime(Vehicle vehicle, City city, LocalDateTime time) {
        return regionalBaseFeeRepository
                .findRbfForParticularTimeByCityIdAndVehicleId(city.getId(), vehicle.getId(), time)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Couldn't find regional base fee for vehicle=" + vehicle + ", city=" + city +
                        ", time=" + time));
    }

    private RegionalBaseFee findActiveRbfByVehicleAndCity(Vehicle vehicle, City city) {
        return regionalBaseFeeRepository
                .findByVehicleIdAndCityIdAndActiveTrue(vehicle.getId(), city.getId())
                .orElseThrow(() -> new EntityNotFoundException("" +
                        "Couldn't find latest active regional base fee for vehicle" +
                        "=" + vehicle + ", city=" + city));
    }

    /**
     * Calculates the fee for a given vehicle and city, optionally considering a specific time.
     * If the time is provided, calls method to fetch regional base fee for that specific time.
     * Otherwise, calls method to fetch currently active regional base fee.
     *
     * @param vehicle the vehicle for which the fee is calculated
     * @param city the city for which the fee is calculated
     * @param time an optional parameter. If provided, the fee will be calculated based on
     *             the fee for that time. Otherwise, the currently active fee will be used.
     * @return the fee amount for the given vehicle and city and time.
     */
    public BigDecimal calculateFeeForVehicleAndCity(Vehicle vehicle, City city, Optional<LocalDateTime> time) {

        RegionalBaseFee regionalBaseFee;

        if (time.isPresent()) regionalBaseFee = findRbfByVehicleAndCityAndTime(vehicle, city, time.get());
        else regionalBaseFee = findActiveRbfByVehicleAndCity(vehicle, city);

        BigDecimal feeAmount = regionalBaseFee.getAmount();
        log.info("regional base fee(city = {}, vehicle={})={}", city, vehicle, feeAmount);
        return feeAmount;
    }

    /**
     * Method for retrieving all regional base fees from database.
     * @param activeOnly if activeOnly is true then retrieves only those
     *                   fees that are currently active.
     * @return List of found regional base fees.
     */
    public List<RegionalBaseFee> findALlRegionalBaseFees(boolean activeOnly) {
        if (activeOnly) return regionalBaseFeeRepository.findAllByActiveTrue();
        return regionalBaseFeeRepository.findAll();
    }

    /**
     * Adds a new regional base fee for a given vehicle and city.
     * If an active regional base fee already exists for the same vehicle and city,
     * it will be deactivated before the new fee is added.
     *
     * This method retrieves the city and vehicle entities using their respective IDs
     * from the provided DTO, checks if an active regional base fee already exists,
     * deactivates it if necessary, and then creates and saves a new regional base fee
     * based on the provided data.
     *
     * @param regionalBaseFeeCreateDTO the data transfer object containing the fee amount,
     *                                 vehicle ID, and city ID used to create
     *                                 the new regional base fee.
     * @return the newly created regional base fee entity after being saved.
     */
    @Transactional
    public RegionalBaseFee addRegionalBaseFee(RegionalBaseFeeCreateDTO regionalBaseFeeCreateDTO) {

        City city = cityService.findCityById(regionalBaseFeeCreateDTO.getCityId());
        Vehicle vehicle = vehicleService.findVehicleById(regionalBaseFeeCreateDTO.getVehicleId());

        Optional<RegionalBaseFee> existingRegionalBaseFeeOptional = regionalBaseFeeRepository
                .findByVehicleIdAndCityIdAndActiveTrue(vehicle.getId(), city.getId());

        if (existingRegionalBaseFeeOptional.isPresent()) {
            deactivateRegionalBaseFee(existingRegionalBaseFeeOptional.get());
        }

        RegionalBaseFee regionalBaseFee = mapper.toRegionalBaseFee(city, vehicle, regionalBaseFeeCreateDTO.getAmount());

        return regionalBaseFeeRepository.save(regionalBaseFee);
    }

    private void deactivateRegionalBaseFee(RegionalBaseFee regionalBaseFee) {
        regionalBaseFee.setActive(false);
        regionalBaseFee.setDeactivatedAt(LocalDateTime.now());
        regionalBaseFeeRepository.save(regionalBaseFee);
    }

    public void deactivateRegionalBaseFeesByCity(City city) {
        regionalBaseFeeRepository
                .findAllByCityAndActiveTrue(city)
                .stream()
                .forEach(this::deactivateRegionalBaseFee);

    }

}

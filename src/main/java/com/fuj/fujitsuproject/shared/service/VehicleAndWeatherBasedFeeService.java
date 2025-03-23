package com.fuj.fujitsuproject.shared.service;

import com.fuj.fujitsuproject.shared.entity.VehicleAndWeatherBasedFee;
import com.fuj.fujitsuproject.shared.exception.FeeAlreadyInactiveException;
import com.fuj.fujitsuproject.shared.exception.FeeToDeactivateNotFound;
import com.fuj.fujitsuproject.shared.exception.VehicleForbiddenException;
import com.fuj.fujitsuproject.shared.repository.VehicleAndWeatherBasedFeeRepository;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.weather.Weather;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Abstract service class for managing vehicle and weather based fees.
 * It provides method implementation for deactivating fee and for calculating delivery
 * fee for a given vehicle, city and optional time.
 *
 * @param <E> the entity type that extends VehicleAndWeatherBasedFee
 * @param <R> the repository type that extends JpaRepository for the entity
 */
public abstract class VehicleAndWeatherBasedFeeService<E extends VehicleAndWeatherBasedFee, R extends VehicleAndWeatherBasedFeeRepository<E>> {

    protected final R repository;

    public VehicleAndWeatherBasedFeeService(R repository) {
        this.repository = repository;
    }

    /**
     * Method that should be implemented by a subclass to provide implementation
     * for finding currently active fee based on provided weather and vehicle.
     * @param vehicle the vehicle to find fee for
     * @param weather the weather to find fee for
     * @return Optional containing the active fee or an empty Optional if not found
     */
    protected abstract Optional<E> findActiveFeeByVehicleAndWeather(Vehicle vehicle, Weather weather);

    /**
     * Method that should be implemented by a subclass to provide implementation
     * for finding fee based on provided weather, vehicle and time.
     * @param vehicle the vehicle to find fee for.
     * @param weather the weather to find fee for.
     * @param time the specific time for which the fee is to be found.
     * @return Optional containing the fee or an empty Optional if not found
     */
    protected abstract Optional<E> findFeeByVehicleAndWeatherAndTime(Vehicle vehicle, Weather weather, LocalDateTime time);

    /**
     * Calculates the fee for a vehicle based on the weather and an optional time parameter.
     * If a time is provided, it will look for the fee at that specific time.
     * Otherwise, it will fetch the active fee.
     * If the vehicle is forbidden, a VehicleForbiddenException is thrown.
     * @param vehicle the vehicle for which the fee is being calculated
     * @param weather the weather conditions for the fee calculation
     * @param time the time for which the fee is to be calculated, if present.
     * @return the calculated fee amount or zero if no fee is found
     * @throws VehicleForbiddenException if usage of the given vehicle is forbidden
     * for the given weather conditions.
     */
    public BigDecimal calculateFee(Vehicle vehicle, Weather weather, Optional<LocalDateTime> time) {
        Optional<E> feeOptional;

        if (time.isPresent()) feeOptional = findFeeByVehicleAndWeatherAndTime(vehicle, weather, time.get());
        else feeOptional = findActiveFeeByVehicleAndWeather(vehicle, weather);

        if (feeOptional.isEmpty()) return BigDecimal.ZERO;

        E fee = feeOptional.get();
        if (fee.isForbidden()) throw new VehicleForbiddenException();

        return fee.getAmount();
    }

    /**
     * Retrieves fees from repository.
     * @param activeOnly If activeOnly is true, retrieves only currently active fees.
     * @return List of found fees.
     */
    public List<E> findAllFees(boolean activeOnly) {
        if (activeOnly) return repository.findAllByActiveTrue();
        return repository.findAll();
    }

    /**
     * Deactivates a fee by its id. The fee will be marked as inactive and a deactivation timestamp will be set.
     * If the fee is already inactive, a FeeAlreadyInactiveException is thrown.
     *
     * @param id the id of the fee to deactivate
     * @throws EntityNotFoundException if the fee with the given id is not found
     * @throws FeeAlreadyInactiveException if the fee is already inactive
     */
    public void deactivateFee(Long id) {
        E fee = repository
                .findById(id)
                .orElseThrow(() -> new FeeToDeactivateNotFound(id));

        if (!fee.isActive()) throw new FeeAlreadyInactiveException();
        fee.setActive(false);
        fee.setDeactivatedAt(LocalDateTime.now());
        repository.save(fee);
    }

    @Transactional
    public void deactivateByVehicleId(Long id) {
        List<E> fees = repository.findByVehicleIdAndActiveTrue(id);
        fees.stream().forEach(fee -> deactivateFee(fee.getId()));
    }
}

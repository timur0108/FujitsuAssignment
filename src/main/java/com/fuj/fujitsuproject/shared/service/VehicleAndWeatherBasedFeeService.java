package com.fuj.fujitsuproject.shared.service;

import com.fuj.fujitsuproject.shared.entity.VehicleAndWeatherBasedFee;
import com.fuj.fujitsuproject.shared.exception.FeeAlreadyInactiveException;
import com.fuj.fujitsuproject.shared.exception.VehicleForbiddenException;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import com.fuj.fujitsuproject.weather.Weather;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public abstract class VehicleAndWeatherBasedFeeService<E extends VehicleAndWeatherBasedFee, R extends JpaRepository<E, Long>> {

    protected final R repository;

    public VehicleAndWeatherBasedFeeService(R repository) {
        this.repository = repository;
    }

    protected abstract Optional<E> findActiveFeeByVehicleAndWeather(Vehicle vehicle, Weather weather);

    protected abstract Optional<E> findFeeByVehicleAndWeatherAndTime(Vehicle vehicle, Weather weather, LocalDateTime time);

    public BigDecimal calculateFee(Vehicle vehicle, Weather weather, Optional<LocalDateTime> time) {
        Optional<E> feeOptional;

        if (time.isPresent()) feeOptional = findFeeByVehicleAndWeatherAndTime(vehicle, weather, time.get());
        else feeOptional = findActiveFeeByVehicleAndWeather(vehicle, weather);

        if (feeOptional.isEmpty()) return BigDecimal.ZERO;

        E fee = feeOptional.get();
        if (fee.isForbidden()) throw new VehicleForbiddenException();

        return fee.getAmount();
    }

    @Transactional
    public void deactivateFee(Long id) {
        E fee = repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        if (!fee.isActive()) throw new FeeAlreadyInactiveException();
        fee.setActive(false);
        fee.setDeactivatedAt(LocalDateTime.now());
    }
}

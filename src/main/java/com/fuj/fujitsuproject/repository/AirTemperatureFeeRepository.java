package com.fuj.fujitsuproject.repository;

import com.fuj.fujitsuproject.entity.AirTemperatureFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AirTemperatureFeeRepository extends JpaRepository<AirTemperatureFee, Long> {

    @Query(value = """
    SELECT * FROM atef
    WHERE vehicle_id = :vehicleId
    AND min_temperature <= :temperature
    AND max_temperature >= :temperature
    AND created_At <= :time
    ORDER BY created_at DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<AirTemperatureFee> findAirTemperatureFeeByVehicleIdAndTemperatureAndTime(
            @Param("vehicleId") Long vehicleId, @Param("temperature")BigDecimal temperature,
            @Param("time")LocalDateTime time);

    @Query(value = """
    SELECT * FROM atef
    WHERE vehicle_id = :vehicleId
    AND min_temperature <= :temperature
    AND max_temperature >= :temperature
    AND active = true
    ORDER BY created_at DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<AirTemperatureFee> findLatestActiveAirTemperatureFeeByVehicleIdAndTemperature(
            @Param("vehicleId") Long vehicleId, @Param("temperature")BigDecimal temperature);
}

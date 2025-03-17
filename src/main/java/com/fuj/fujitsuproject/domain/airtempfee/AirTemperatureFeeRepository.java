package com.fuj.fujitsuproject.domain.airtempfee;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AirTemperatureFeeRepository extends JpaRepository<AirTemperatureFee, Long> {

    @Query(value = """
    SELECT * FROM atef
    WHERE vehicle_id = :vehicleId
    AND min_temperature <= :temperature
    AND max_temperature >= :temperature
    AND created_At <= :time
    AND (deactivated_at IS NULL OR deactivated_at  > :time)
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

    @Query(value = """
    SELECT * FROM atef
    WHERE active = true
    AND vehicle_id = :vehicleId
    AND min_temperature <= :maxTemperature
    AND max_temperature >= :minTemperature
""", nativeQuery = true)
    List<AirTemperatureFee> findActiveAirTemperatureFeeWithOverlappingTemperatureRangeByVehicleId(
            @Param("minTemperature") BigDecimal minTemperature,
            @Param("maxTemperature") BigDecimal maxTemperature,
            @Param("vehicleId") Long vehicleId);

    @EntityGraph(attributePaths = {"vehicle"})
    List<AirTemperatureFee> findAll();
}

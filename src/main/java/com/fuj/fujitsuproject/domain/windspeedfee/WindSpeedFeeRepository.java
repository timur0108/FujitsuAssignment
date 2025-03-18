package com.fuj.fujitsuproject.domain.windspeedfee;

import com.fuj.fujitsuproject.domain.airtempfee.AirTemperatureFee;
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
public interface WindSpeedFeeRepository extends JpaRepository<WindSpeedFee, Long> {

    @Query(value = """
    SELECT * FROM wsef
    WHERE vehicle_id = :vehicleId
    AND min_speed <= :speed
    AND max_speed >= :speed
    AND created_at <= :time
    AND (deactivated_at IS NULL OR deactivated_at  > :time)
    ORDER BY created_at DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<WindSpeedFee> findWindSpeedFeeByVehicleIdAndWindSpeedAndTime(
            @Param("vehicleId") Long vehicleId, @Param("speed") BigDecimal speed,
            @Param("time") LocalDateTime time);

    @Query(value = """
    SELECT * FROM wsef
    WHERE vehicle_id = :vehicleId
    AND min_speed <= :speed
    AND max_speed >= :speed
    AND active = true
    ORDER BY created_at DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<WindSpeedFee> findLatestActiveWindSpeedFeeByVehicleIdAndSpeed(
            @Param("vehicleId") Long vehicleId, @Param("speed") BigDecimal speed);

    @Query(value = """
    SELECT * FROM wsef
    WHERE active = true
    AND vehicle_id = :vehicleId
    AND min_speed <= :maxSpeed
    AND max_speed >= :minSpeed
""", nativeQuery = true)
    List<WindSpeedFee> findActiveWindSpeedFeesWithOverlappingSpeedRangeByVehicleId(
            @Param("minSpeed") BigDecimal minSpeed, @Param("maxSpeed") BigDecimal maxSpeed,
            @Param("vehicleId") Long vehicleId);

    @EntityGraph(attributePaths = {"vehicle"})
    List<WindSpeedFee> findAll();
}

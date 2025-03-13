package com.fuj.fujitsuproject.repository;

import com.fuj.fujitsuproject.entity.AirTemperatureFee;
import com.fuj.fujitsuproject.entity.WindSpeedFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WindSpeedFeeRepository extends JpaRepository<WindSpeedFee, Long> {

    @Query(value = """
    SELECT * FROM wsef
    WHERE vehicle_id = :vehicleId
    AND min_speed <= :speed
    AND max_speed >= :speed
    AND created_At <= :time
    ORDER BY created_at DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<WindSpeedFee> findWindSpeedFeeByVehicleIdAndWindSpeedAndTime(
            @Param("vehicleId") Long vehicleId, @Param("speed") BigDecimal speed,
            @Param("time") LocalDateTime time);
}

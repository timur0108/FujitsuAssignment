package com.fuj.fujitsuproject.repository;

import com.fuj.fujitsuproject.entity.WeatherPhenomenonFee;
import com.fuj.fujitsuproject.entity.WindSpeedFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WeatherPhenomenonFeeRepository extends JpaRepository<WeatherPhenomenonFee, Long> {

    @Query(value = """
    SELECT * FROM wpef
    WHERE vehicle_id = :vehicleId
    AND phenomenon LIKE %:weatherPhenomenon%
    AND created_At <= :time
    ORDER BY created_at DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<WeatherPhenomenonFee> findWeatherPhenomenonFeeByVehicleIdAndPhenomenonAndTime(
            @Param("vehicleId") Long vehicleId, @Param("weatherPhenomenon") String weatherPhenomenon,
            @Param("time") LocalDateTime time);
}

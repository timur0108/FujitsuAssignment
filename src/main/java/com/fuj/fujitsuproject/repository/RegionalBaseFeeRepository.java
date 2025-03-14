package com.fuj.fujitsuproject.repository;

import com.fuj.fujitsuproject.entity.RegionalBaseFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RegionalBaseFeeRepository extends JpaRepository<RegionalBaseFee, Long> {

    @Query(value = """
    SELECT * FROM rbf
    WHERE vehicle_id = :vehicleId
    AND city_id = :cityId
    AND created_at <= :time
    ORDER BY created_at DESC
    LIMIT 1""", nativeQuery = true)
    Optional<RegionalBaseFee> findRbfForParticularTimeByCityIdAndVehicleId(
            @Param("cityId") Long cityId, @Param("vehicleId") Long vehicleId,
            @Param("time")LocalDateTime time);

    @Query(value = """
    SELECT * FROM rbf
    WHERE vehicle_id = :vehicleId
    AND city_id = :cityId
    AND active = true
    ORDER BY created_at DESC
    LIMIT 1""", nativeQuery = true)
    Optional<RegionalBaseFee> findLatestActiveRbfByCityIdAndVehicleId(
            @Param("cityId") Long cityId, @Param("vehicleId") Long vehicleId);
}

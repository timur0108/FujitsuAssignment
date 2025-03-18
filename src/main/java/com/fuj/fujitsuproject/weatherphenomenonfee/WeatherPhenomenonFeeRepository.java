package com.fuj.fujitsuproject.weatherphenomenonfee;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherPhenomenonFeeRepository extends JpaRepository<WeatherPhenomenonFee, Long> {

    @Query(value = """
    SELECT * FROM wpef
    WHERE vehicle_id = :vehicleId
    AND :weatherPhenomenon LIKE CONCAT('%', phenomenon, '%')
    AND created_at <= :time
    AND (deactivated_at IS NULL OR deactivated_at  > :time)
    ORDER BY created_at DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<WeatherPhenomenonFee> findWeatherPhenomenonFeeByVehicleIdAndPhenomenonAndTime(
            @Param("vehicleId") Long vehicleId, @Param("weatherPhenomenon") String weatherPhenomenon,
            @Param("time") LocalDateTime time);

    @Query(value = """
    SELECT * FROM wpef
    WHERE vehicle_id = :vehicleId
    AND :weatherPhenomenon LIKE CONCAT('%', phenomenon, '%')
    AND active = true
    ORDER BY created_at DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<WeatherPhenomenonFee> findLatestWeatherPhenomenonFeeByVehicleIdAndPhenomenon(
            @Param("vehicleId") Long vehicleId, @Param("weatherPhenomenon") String weatherPhenomenon);

    Optional<WeatherPhenomenonFee> findWeatherPhenomenonFeeByPhenomenonAndActiveTrue(
            String phenomenon);

    @EntityGraph(attributePaths = {"vehicle"})
    List<WeatherPhenomenonFee> findAll();
}

package com.fuj.fujitsuproject.regionalbasefee;

import com.fuj.fujitsuproject.city.City;
import com.fuj.fujitsuproject.vehicle.Vehicle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegionalBaseFeeRepository extends JpaRepository<RegionalBaseFee, Long> {

    @Query(value = """
    SELECT * FROM rbf
    WHERE vehicle_id = :vehicleId
    AND city_id = :cityId
    AND created_at <= :time
    AND (deactivated_at IS NULL OR deactivated_at  > :time)
    ORDER BY created_at DESC
    LIMIT 1""", nativeQuery = true)
    Optional<RegionalBaseFee> findRbfForParticularTimeByCityIdAndVehicleId(
            @Param("cityId") Long cityId, @Param("vehicleId") Long vehicleId,
            @Param("time")LocalDateTime time);

    @EntityGraph(attributePaths = {"city", "vehicle"})
    List<RegionalBaseFee> findAll();

    @EntityGraph(attributePaths = {"city", "vehicle"})
    List<RegionalBaseFee> findAllByActiveTrue();

    Optional<RegionalBaseFee> findByVehicleIdAndCityIdAndActiveTrue(Long vehicleId, Long cityId);

    List<RegionalBaseFee> findAllByCityAndActiveTrue(City city);

    List<RegionalBaseFee> findALlByVehicleIdAndActiveTrue(Long vehicleId);
}

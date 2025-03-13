package com.fuj.fujitsuproject.repository;

import com.fuj.fujitsuproject.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    @Query(value = """
    SELECT * FROM weather
    WHERE station_name = :stationName
    AND observation_timestamp <= :time
    ORDER BY observation_timestamp DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<Weather> findWeatherByStationAndTime(@Param("stationName") String stationName,
    @Param("time")LocalDateTime time);
}
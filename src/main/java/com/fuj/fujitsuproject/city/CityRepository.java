package com.fuj.fujitsuproject.city;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query("""
    SELECT DISTINCT c.stationName 
    FROM City c
    WHERE c.deleted = false
""")
    List<String> findAllStationName();

    List<City> findAllByDeletedFalse();

    Optional<City> findByNameEquals(String name);

    Optional<City> findCityByNameEqualsAndDeletedFalse(String name);

    @Query("""
    SELECT c FROM City c
    WHERE c.createdAt <= :time
    AND (c.deletedAt IS NULL OR c.deletedAt > :time)
    AND c.name = :name
""")
    Optional<City> findCityByNameAndTime(String name, LocalDateTime time);
}

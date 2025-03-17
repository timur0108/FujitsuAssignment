package com.fuj.fujitsuproject.domain.city;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query("""
    SELECT DISTINCT c.stationName 
    FROM City c
""")
    List<String> findAllStationName();

    Optional<City> findByNameEquals(String name);
}

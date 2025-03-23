package com.fuj.fujitsuproject.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByNameEqualsIgnoreCase(String name);

    Optional<Vehicle> findByNameEqualsIgnoreCaseAndDeletedFalse(String name);

    @Query("""
    SELECT v FROM Vehicle v
    WHERE LOWER(v.name) = LOWER(:name)
    AND v.createdAt <= :time
    AND (v.deletedAt IS NULL OR v.deletedAt > :time) 
""")
    Optional<Vehicle> findByNameAndTime(String name, LocalDateTime time);

    List<Vehicle> findAllByDeletedFalse();
}

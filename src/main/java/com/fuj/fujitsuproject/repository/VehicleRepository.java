package com.fuj.fujitsuproject.repository;

import com.fuj.fujitsuproject.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByNameEqualsIgnoreCase(String name);
}

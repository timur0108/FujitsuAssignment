package com.fuj.fujitsuproject.shared.repository;

import com.fuj.fujitsuproject.shared.entity.VehicleAndWeatherBasedFee;
import com.fuj.fujitsuproject.weatherphenomenonfee.WeatherPhenomenonFee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface VehicleAndWeatherBasedFeeRepository<E extends VehicleAndWeatherBasedFee> extends JpaRepository<E, Long> {

    @EntityGraph(attributePaths = {"vehicle"})
    List<E> findAll();

    @EntityGraph(attributePaths = {"vehicle"})
    List<E> findAllByActiveTrue();
}

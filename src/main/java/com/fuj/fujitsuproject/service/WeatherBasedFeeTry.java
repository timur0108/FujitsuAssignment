package com.fuj.fujitsuproject.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class WeatherBasedFeeTry<T, Long> {

    protected abstract JpaRepository<T, Long> getRepository();

    public Optional<T> findById(Long id) {
        return getRepository().findById(id);
    }

    public T save (T entity) {
        return getRepository().save(entity);
    }

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public
}

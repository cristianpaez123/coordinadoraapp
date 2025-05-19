package com.example.coordinadoraapp.domain.repository;

import com.example.coordinadoraapp.domain.model.Location;

import io.reactivex.rxjava3.core.Single;

public interface AddLocationRepository {
    Single<Location> addLocation(String base64);
}

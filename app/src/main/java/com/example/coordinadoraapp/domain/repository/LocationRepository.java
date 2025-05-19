package com.example.coordinadoraapp.domain.repository;

import com.example.coordinadoraapp.domain.model.Location;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface LocationRepository {
    Completable saveLocation(Location location);
    Single<List<Location>> getAllLocations();
}


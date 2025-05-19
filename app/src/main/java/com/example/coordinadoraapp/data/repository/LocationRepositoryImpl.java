package com.example.coordinadoraapp.data.repository;

import com.example.coordinadoraapp.data.local.LocationLocalDataSource;
import com.example.coordinadoraapp.domain.model.Location;
import com.example.coordinadoraapp.domain.repository.LocationRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class LocationRepositoryImpl implements LocationRepository {

    private final LocationLocalDataSource localDataSource;

    @Inject
    public LocationRepositoryImpl(LocationLocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    @Override
    public Completable saveLocation(Location location) {
        return localDataSource.saveLocation(location);
    }
}


package com.example.coordinadoraapp.data.repository;

import com.example.coordinadoraapp.data.local.LocationLocalDataSource;
import com.example.coordinadoraapp.domain.model.Location;
import com.example.coordinadoraapp.domain.repository.LocationRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

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

    @Override
    public Completable saveAllLocations(List<Location> locations) {
        return localDataSource.saveAllLocations(locations);
    }

    @Override
    public Single<List<Location>> getAllLocations() {
        return localDataSource.getAll();
    }

    @Override
    public Completable clearAllLocations() {
        return localDataSource.clearAll();
    }

    @Override
    public Observable<Integer> getLocationCountStream() {
        return localDataSource.observeLocationCount();
    }

}

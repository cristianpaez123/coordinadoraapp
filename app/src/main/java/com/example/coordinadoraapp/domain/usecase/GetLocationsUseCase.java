package com.example.coordinadoraapp.domain.usecase;

import com.example.coordinadoraapp.domain.model.Location;
import com.example.coordinadoraapp.domain.repository.LocationRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetLocationsUseCase {
    private final LocationRepository repository;

    @Inject
    public GetLocationsUseCase(LocationRepository repository) {
        this.repository = repository;
    }

    public Single<List<Location>> execute() {
        return repository.getAllLocations();
    }
}

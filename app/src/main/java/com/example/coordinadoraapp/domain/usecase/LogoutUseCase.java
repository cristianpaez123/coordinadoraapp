package com.example.coordinadoraapp.domain.usecase;

import com.example.coordinadoraapp.domain.repository.AuthRepository;
import com.example.coordinadoraapp.domain.repository.LocationRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class LogoutUseCase {

    private final AuthRepository authRepository;
    private final LocationRepository locationRepository;

    @Inject
    public LogoutUseCase(AuthRepository authRepository, LocationRepository locationRepository) {
        this.authRepository = authRepository;
        this.locationRepository = locationRepository;
    }

    public Completable execute() {
        return locationRepository.clearAllLocations()
            .andThen(Completable.fromAction(authRepository::logout));
    }
}

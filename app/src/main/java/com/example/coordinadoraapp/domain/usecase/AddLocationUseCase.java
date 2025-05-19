package com.example.coordinadoraapp.domain.usecase;

import com.example.coordinadoraapp.domain.model.Location;
import com.example.coordinadoraapp.domain.repository.AddLocationRepository;
import com.example.coordinadoraapp.domain.repository.LocationRepository;
import com.example.coordinadoraapp.domain.repository.RemoteLocationBackupRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class AddLocationUseCase {

    private final AddLocationRepository validationRepository;
    private final LocationRepository locationRepository;
    private final RemoteLocationBackupRepository remoteBackupRepository;

    @Inject
    public AddLocationUseCase(
        AddLocationRepository validationRepository,
        LocationRepository locationRepository,
        RemoteLocationBackupRepository remoteBackupRepository
    ) {
        this.validationRepository = validationRepository;
        this.locationRepository = locationRepository;
        this.remoteBackupRepository = remoteBackupRepository;
    }

    public Single<Location> execute(String base64) {
        return validationRepository.addLocation(base64)
            .flatMap(location ->
                locationRepository.saveLocation(location)
                    .andThen(Single.just(location))
            );
    }
}


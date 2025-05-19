package com.example.coordinadoraapp.domain.usecase;

import com.example.coordinadoraapp.domain.model.Location;
import com.example.coordinadoraapp.domain.repository.LocationRepository;
import com.example.coordinadoraapp.domain.repository.RawInputValidationRepository;
import com.example.coordinadoraapp.domain.repository.RemoteLocationBackupRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ValidateRawInputUseCase {

    private final RawInputValidationRepository validationRepository;
    private final LocationRepository locationRepository;
    private final RemoteLocationBackupRepository remoteBackupRepository;

    @Inject
    public ValidateRawInputUseCase(
        RawInputValidationRepository validationRepository,
        LocationRepository locationRepository,
        RemoteLocationBackupRepository remoteBackupRepository
    ) {
        this.validationRepository = validationRepository;
        this.locationRepository = locationRepository;
        this.remoteBackupRepository = remoteBackupRepository;
    }

    public Single<Location> execute(String rawText) {
        return validationRepository.submitEncodedData(rawText)
            .flatMap(location ->
                locationRepository.saveLocation(location)
                    .andThen(remoteBackupRepository.backupLocation(location))
                    .andThen(Single.just(location))
            );
    }
}


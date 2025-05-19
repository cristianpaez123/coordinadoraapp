package com.example.coordinadoraapp.domain.usecase;

import com.example.coordinadoraapp.domain.model.Location;
import com.example.coordinadoraapp.domain.repository.LocationRepository;
import com.example.coordinadoraapp.domain.repository.RawInputValidationRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ValidateRawInputUseCase {

    private final RawInputValidationRepository validationRepository;
    private final LocationRepository locationRepository;

    @Inject
    public ValidateRawInputUseCase(
        RawInputValidationRepository validationRepository,
        LocationRepository locationRepository
    ) {
        this.validationRepository = validationRepository;
        this.locationRepository = locationRepository;
    }

    public Single<Location> execute(String rawText) {
        return validationRepository.submitEncodedData(rawText)
            .flatMap(location ->
                locationRepository.saveLocation(location)
                    .andThen(Single.just(location))
            );
    }
}

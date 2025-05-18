package com.example.coordinadoraapp.domain.usecase;

import com.example.coordinadoraapp.domain.model.Location;
import com.example.coordinadoraapp.domain.repository.RawInputValidationRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ValidateRawInputUseCase {

    private final RawInputValidationRepository repository;

    @Inject
    public ValidateRawInputUseCase(RawInputValidationRepository repository) {
        this.repository = repository;
    }

    public Single<Location> execute(String rawText) {
        return repository.submitEncodedData(rawText);
    }
}
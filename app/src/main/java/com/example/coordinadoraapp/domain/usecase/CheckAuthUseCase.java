package com.example.coordinadoraapp.domain.usecase;

import com.example.coordinadoraapp.domain.repository.AuthRepository;

import javax.inject.Inject;

public class CheckAuthUseCase {

    private final AuthRepository authRepository;

    @Inject
    public CheckAuthUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public boolean execute() {
        return authRepository.isUserAuthenticated();
    }
}


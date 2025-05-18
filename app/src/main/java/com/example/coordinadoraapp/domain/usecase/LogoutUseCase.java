package com.example.coordinadoraapp.domain.usecase;

import com.example.coordinadoraapp.domain.repository.AuthRepository;

import javax.inject.Inject;

public class LogoutUseCase {

    private final AuthRepository authRepository;

    @Inject
    public LogoutUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute() {
        authRepository.logout();
    }
}


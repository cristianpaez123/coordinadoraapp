package com.example.coordinadoraapp.domain.usecase;

import com.example.coordinadoraapp.domain.repository.AuthRepository;
import com.google.firebase.auth.AuthResult;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class LoginUseCase {

    private final AuthRepository authRepository;

    @Inject
    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public Single<AuthResult> execute(String email, String password) {
        return authRepository.login(email, password);
    }
}

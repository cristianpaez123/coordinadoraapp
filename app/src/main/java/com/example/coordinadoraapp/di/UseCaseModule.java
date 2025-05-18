package com.example.coordinadoraapp.di;

import com.example.coordinadoraapp.domain.repository.AuthRepository;
import com.example.coordinadoraapp.domain.usecase.CheckAuthUseCase;
import com.example.coordinadoraapp.domain.usecase.LoginUseCase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UseCaseModule {

    @Provides
    @Singleton
    public LoginUseCase provideLoginUseCase(AuthRepository authRepository) {
        return new LoginUseCase(authRepository);
    }
    @Provides
    @Singleton
    public CheckAuthUseCase provideCheckAuthUseCase(AuthRepository authRepository) {
        return new CheckAuthUseCase(authRepository);
    }
}

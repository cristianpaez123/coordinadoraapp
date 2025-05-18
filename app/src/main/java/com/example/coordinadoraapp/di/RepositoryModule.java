package com.example.coordinadoraapp.di;

import com.example.coordinadoraapp.data.repository.AuthRepositoryImpl;
import com.example.coordinadoraapp.domain.repository.AuthRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public AuthRepository provideAuthRepository(AuthRepositoryImpl impl) {
        return impl;
    }
}

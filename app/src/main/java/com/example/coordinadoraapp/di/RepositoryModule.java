package com.example.coordinadoraapp.di;

import com.example.coordinadoraapp.data.repository.AddLocationRepositoryImpl;
import com.example.coordinadoraapp.data.repository.AuthRepositoryImpl;
import com.example.coordinadoraapp.data.repository.LocationRepositoryImpl;
import com.example.coordinadoraapp.data.repository.RemoteLocationBackupRepositoryImpl;
import com.example.coordinadoraapp.domain.repository.AddLocationRepository;
import com.example.coordinadoraapp.domain.repository.AuthRepository;
import com.example.coordinadoraapp.domain.repository.LocationRepository;
import com.example.coordinadoraapp.domain.repository.RemoteLocationBackupRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {

    @Binds
    @Singleton
    public abstract AuthRepository bindAuthRepository(AuthRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract AddLocationRepository bindRawInputValidationRepository(
        AddLocationRepositoryImpl impl
    );

    @Binds
    @Singleton
    public abstract LocationRepository bindLocationRepository(
        LocationRepositoryImpl impl
    );

    @Binds
    @Singleton
    public abstract RemoteLocationBackupRepository bindRemoteLocationBackupRepository(
        RemoteLocationBackupRepositoryImpl impl
    );
}
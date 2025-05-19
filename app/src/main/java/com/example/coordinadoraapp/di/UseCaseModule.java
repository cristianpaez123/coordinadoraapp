package com.example.coordinadoraapp.di;

import com.example.coordinadoraapp.domain.repository.MainRepository;
import com.example.coordinadoraapp.domain.usecase.QrAnalyzerUseCase;
import com.example.coordinadoraapp.domain.usecase.StartQrScannerUseCase;
import com.example.coordinadoraapp.domain.repository.AuthRepository;
import com.example.coordinadoraapp.domain.usecase.GetMapPolylineUseCase;
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
    public QrAnalyzerUseCase provideQrAnalyzerUC(MainRepository repository) {
        return new QrAnalyzerUseCase(repository);
    }

    @Provides
    @Singleton
    StartQrScannerUseCase provideStartQrScannerUseCase() {
        return new StartQrScannerUseCase();
    }

    @Provides
    GetMapPolylineUseCase provideGetMapPolylineUseCase() {
        return new GetMapPolylineUseCase();
    }
}

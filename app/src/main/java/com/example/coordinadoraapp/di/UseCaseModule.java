package com.example.coordinadoraapp.di;

import com.example.coordinadoraapp.domain.mainActivity.MainActivityRepository;
import com.example.coordinadoraapp.domain.mainActivity.QrAnalyzerUC;
import com.example.coordinadoraapp.domain.mainActivity.StartQrScannerUseCase;
import com.example.coordinadoraapp.domain.repository.AuthRepository;
import com.example.coordinadoraapp.domain.usecase.LoginUseCase;
import com.example.coordinadoraapp.ui.mainActivity.MainActivityViewModel;
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
    public QrAnalyzerUC provideQrAnalyzerUC(MainActivityRepository repository) {
        return new QrAnalyzerUC(repository);
    }

    @Provides
    @Singleton
    StartQrScannerUseCase provideStartQrScannerUseCase() {
        return new StartQrScannerUseCase();
    }
}

package com.example.coordinadoraapp.di;

import com.example.coordinadoraapp.domain.mainActivity.MainActivityRepository;
import com.example.coordinadoraapp.domain.mainActivity.QrAnalyzerUseCase;
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
    public QrAnalyzerUseCase provideQrAnalyzerUseCase(
            MainActivityRepository repository,
            MainActivityViewModel viewModel
    ) {
        return new QrAnalyzerUseCase(repository,viewModel);
    }
}

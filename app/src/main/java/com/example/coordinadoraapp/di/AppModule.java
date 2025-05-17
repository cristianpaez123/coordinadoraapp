package com.example.coordinadoraapp.di;

import android.content.Context;

import com.example.coordinadoraapp.data.repository.AuthRepository;
import com.example.coordinadoraapp.domain.usecase.LoginUseCase;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    public LoginUseCase provideLoginUseCase(AuthRepository authRepository) {
        return new LoginUseCase(authRepository);
    }
}

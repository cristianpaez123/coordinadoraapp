package com.example.coordinadoraapp.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.coordinadoraapp.domain.usecase.LoginUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private final LoginUseCase loginUseCase;

    @Inject
    public LoginViewModelFactory(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoginViewModel(loginUseCase);
    }
}

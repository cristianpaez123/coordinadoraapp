package com.example.coordinadoraapp.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.coordinadoraapp.data.repository.AuthRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;

public class LoginViewModelFactory implements ViewModelProvider.Factory{

    private final AuthRepository authRepository;

    @Inject
    public LoginViewModelFactory(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoginViewModel(authRepository);
    }

}

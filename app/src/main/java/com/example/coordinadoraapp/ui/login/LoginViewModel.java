package com.example.coordinadoraapp.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coordinadoraapp.domain.usecase.LoginUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private final LoginUseCase loginUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<LoginUiState> _uiState = new MutableLiveData<>();
    public final LiveData<LoginUiState> uiState = _uiState;

    @Inject
    public LoginViewModel(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    public void login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            _uiState.setValue(new LoginUiState.Error("Correo y contraseña son obligatorios"));
            return;
        }

        _uiState.setValue(new LoginUiState.Loading());

        disposables.add(
            loginUseCase.execute(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    authResult -> {
                        _uiState.setValue(new LoginUiState.Success());
                    },
                    throwable -> {
                        String message = throwable.getMessage() != null
                            ? throwable.getMessage()
                            : "Error desconocido";
                        _uiState.setValue(new LoginUiState.Error(message));
                    }
                )
        );

    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }

    public static abstract class LoginUiState {
        public static class Loading extends LoginUiState {
        }

        public static class Success extends LoginUiState {
        }

        public static class Error extends LoginUiState {
            public final String message;

            public Error(String message) {
                this.message = message;
            }
        }
    }
}


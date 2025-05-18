package com.example.coordinadoraapp.ui.login;

import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coordinadoraapp.domain.usecase.LoginUseCase;
import com.example.coordinadoraapp.utils.CredentialValidator;
import com.example.coordinadoraapp.utils.FirebaseErrorMapper;

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
        Integer errorResId = CredentialValidator.validate(email, password);
        if (errorResId != null) {
            _uiState.setValue(new LoginUiState.Error(errorResId));
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
                        int messageRes = FirebaseErrorMapper.map(throwable);
                        _uiState.setValue(new LoginUiState.Error(messageRes));
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
            @StringRes
            public final Integer messageRes;
            public final String message;

            public Error(@StringRes int messageRes) {
                this.messageRes = messageRes;
                this.message = null;
            }

            public Error(String message) {
                this.message = message;
                this.messageRes = null;
            }
        }
    }
}


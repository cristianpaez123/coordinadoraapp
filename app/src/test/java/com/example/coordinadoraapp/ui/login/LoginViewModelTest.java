package com.example.coordinadoraapp.ui.login;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.coordinadoraapp.domain.usecase.LoginUseCase;
import com.example.coordinadoraapp.ui.login.LoginViewModel.LoginUiState;
import com.google.firebase.auth.AuthResult;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private LoginUseCase loginUseCase;
    private LoginViewModel viewModel;
    private Observer<LoginUiState> observer;

    @Before
    public void setUp() {
        loginUseCase = mock(LoginUseCase.class);
        observer = mock(Observer.class);

        // ✅ Inyectamos los Schedulers directamente
        viewModel = new LoginViewModel(loginUseCase, Schedulers.trampoline(), Schedulers.trampoline());
        viewModel.uiState.observeForever(observer);
    }

    @Test
    public void login_withInvalidCredentials_emitsErrorState() {
        viewModel.login("", "");

        verify(observer).onChanged(any(LoginUiState.Error.class));
        verifyNoMoreInteractions(loginUseCase);
    }

    @Test
    public void login_withValidCredentials_successfulLogin_emitsLoadingAndSuccess() {
        AuthResult mockAuthResult = mock(AuthResult.class);
        when(loginUseCase.execute("test@example.com", "password"))
            .thenReturn(Single.just(mockAuthResult));

        viewModel.login("test@example.com", "password");

        verify(observer).onChanged(any(LoginUiState.Loading.class));
        verify(observer).onChanged(any(LoginUiState.Success.class));
        verify(loginUseCase).execute("test@example.com", "password");
    }

    @Test
    public void login_withValidCredentials_errorLogin_emitsLoadingAndError() {
        when(loginUseCase.execute("test@example.com", "password"))
            .thenReturn(Single.error(new RuntimeException("Firebase error")));

        viewModel.login("test@example.com", "password");

        verify(observer).onChanged(any(LoginUiState.Loading.class));
        verify(observer, atLeastOnce()).onChanged(argThat(state -> state instanceof LoginUiState.Error));
        verify(loginUseCase).execute("test@example.com", "password");
    }
}

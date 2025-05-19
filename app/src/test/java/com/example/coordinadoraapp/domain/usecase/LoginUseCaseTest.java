package com.example.coordinadoraapp.domain.usecase;

import com.example.coordinadoraapp.domain.repository.AuthRepository;
import com.example.coordinadoraapp.domain.usecase.LoginUseCase;
import com.google.firebase.auth.AuthResult;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.TestObserver;

import static org.mockito.Mockito.*;

public class LoginUseCaseTest {

    private AuthRepository authRepository;
    private LoginUseCase loginUseCase;

    @Before
    public void setUp() {
        authRepository = mock(AuthRepository.class);
        loginUseCase = new LoginUseCase(authRepository);
    }

    @Test
    public void execute_whenCalled_shouldCallAuthRepository() {
        String email = "test@example.com";
        String password = "password123";
        AuthResult mockResult = mock(AuthResult.class);

        when(authRepository.login(email, password))
            .thenReturn(Single.just(mockResult));

        TestObserver<AuthResult> testObserver = loginUseCase.execute(email, password).test();

        testObserver.assertValue(mockResult);
        testObserver.assertComplete();
        verify(authRepository).login(email, password);
    }

    @Test
    public void execute_whenAuthFails_shouldEmitError() {
        String email = "test@example.com";
        String password = "wrongpassword";
        Throwable error = new RuntimeException("auth error");

        when(authRepository.login(email, password))
            .thenReturn(Single.error(error));

        TestObserver<AuthResult> testObserver = loginUseCase.execute(email, password).test();

        testObserver.assertError(error);
        verify(authRepository).login(email, password);
    }
}

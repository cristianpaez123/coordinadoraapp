package com.example.coordinadoraapp.ui.mainActivity;

import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coordinadoraapp.domain.mainActivity.MainActivityRepository;
import com.example.coordinadoraapp.domain.usecase.LogoutUseCase;
import com.example.coordinadoraapp.domain.usecase.ValidateRawInputUseCase;
import com.example.coordinadoraapp.ui.login.LoginViewModel;
import com.example.coordinadoraapp.ui.mapper.LocationUiMapper;
import com.example.coordinadoraapp.ui.model.LocationUi;
import com.google.mlkit.vision.common.InputImage;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<String> qrResult = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutSuccess = new MutableLiveData<>();

    private final MutableLiveData<RawInputUiState> _rawInputUiState = new MutableLiveData<>();
    public final LiveData<RawInputUiState> rawInputUiState = _rawInputUiState;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MainActivityRepository repository;
    private final LogoutUseCase logoutUseCase;
    private final ValidateRawInputUseCase validateRawInputUseCase;


    @Inject
    public MainActivityViewModel(MainActivityRepository repository, LogoutUseCase logoutUseCase, ValidateRawInputUseCase validateRawInputUseCase) {
        this.repository = repository;
        this.logoutUseCase = logoutUseCase;
        this.validateRawInputUseCase = validateRawInputUseCase;
    }

    public LiveData<String> getQrResult() { return qrResult; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getLogoutSuccess() { return logoutSuccess; }

    public void processImage(InputImage image) {
        disposables.add(repository.processImage(image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                result -> qrResult.setValue(result),
                throwable -> error.setValue(throwable.getMessage())
            ));
    }

    public void submitEncodedText(String rawText) {
        _rawInputUiState.setValue(new RawInputUiState.Loading());
        disposables.add(validateRawInputUseCase.execute(rawText)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                validatedData -> {
                    _rawInputUiState.setValue(new RawInputUiState.Success(LocationUiMapper.toUi(validatedData)));
                },
                throwable -> {
                    _rawInputUiState.setValue(new RawInputUiState.Error(throwable.getMessage()));
                }
            ));
    }

    public void logout() {
        logoutUseCase.execute();
        logoutSuccess.setValue(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public static abstract class RawInputUiState {
        public static class Loading extends RawInputUiState { }
        public static class Success extends RawInputUiState {
            public final LocationUi locationUi;
            public Success(LocationUi locationUi) {
                this.locationUi = locationUi;
            }
        }

        public static class Error extends RawInputUiState {
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

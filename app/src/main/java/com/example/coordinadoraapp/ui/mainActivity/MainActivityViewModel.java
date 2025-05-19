package com.example.coordinadoraapp.ui.mainActivity;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;

import androidx.camera.view.PreviewView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coordinadoraapp.domain.mainActivity.QrAnalyzerUC;
import com.example.coordinadoraapp.domain.mainActivity.QrResultListener;
import com.example.coordinadoraapp.domain.mainActivity.StartQrScannerUseCase;
import com.example.coordinadoraapp.domain.usecase.GetLocationsUseCase;
import com.example.coordinadoraapp.domain.usecase.LogoutUseCase;
import com.example.coordinadoraapp.domain.usecase.ValidateRawInputUseCase;
import com.example.coordinadoraapp.ui.mainActivity.state.LocationsUiState;
import com.example.coordinadoraapp.ui.mainActivity.state.RawInputUiState;
import com.example.coordinadoraapp.ui.mapper.LocationUiMapper;

import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isQrVisible = new MutableLiveData<>(false);

    private final QrAnalyzerUC analyzer;
    private final StartQrScannerUseCase startScannerUseCase;
    private final GetLocationsUseCase getLocationsUseCase;
    private final MutableLiveData<Boolean> logoutSuccess = new MutableLiveData<>();

    private final MutableLiveData<RawInputUiState> _rawInputUiState = new MutableLiveData<>();
    public final LiveData<RawInputUiState> rawInputUiState = _rawInputUiState;

    private final MutableLiveData<LocationsUiState> _locationsState = new MutableLiveData<>();
    public final LiveData<LocationsUiState> locationsState = _locationsState;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final LogoutUseCase logoutUseCase;
    private final ValidateRawInputUseCase validateRawInputUseCase;

    public LiveData<Boolean> getIsQrVisible() {
        return isQrVisible;
    }

    @Inject
    public MainActivityViewModel(
        LogoutUseCase logoutUseCase,
        ValidateRawInputUseCase validateRawInputUseCase,
        QrAnalyzerUC analyzer,
        StartQrScannerUseCase startScannerUseCase,
        GetLocationsUseCase getLocationsUseCase
    ) {
        this.logoutUseCase = logoutUseCase;
        this.validateRawInputUseCase = validateRawInputUseCase;
        this.analyzer = analyzer;
        this.startScannerUseCase = startScannerUseCase;
        this.getLocationsUseCase = getLocationsUseCase;
        loadLocations();
    }

    public LiveData<Boolean> getLogoutSuccess() {
        return logoutSuccess;
    }

    public void initQrScanner(Context context, LifecycleOwner lifecycleOwner, PreviewView previewView, RectF guideRect) {
        analyzer.setGuideRect(guideRect);
        analyzer.setListener(new QrResultListener() {
            @Override
            public void onQrDetected(String value) {
                isQrVisible.postValue(true);
                Log.d("QR_VALUE", "QR detectado: " + value);
            }

            @Override
            public void onQrNotDetected() {
                isQrVisible.postValue(false);
            }
        });
        startScannerUseCase.execute(context, lifecycleOwner, previewView, analyzer);
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

    private void loadLocations() {
        _locationsState.setValue(new LocationsUiState.Loading());
        disposables.add(
            getLocationsUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(locations -> locations.stream()
                    .map(LocationUiMapper::toUi)
                    .collect(Collectors.toList()))
                .subscribe(
                    result -> _locationsState.setValue(new LocationsUiState.Success(result)),
                    error -> _locationsState.setValue(new LocationsUiState.Error(error.getMessage()))
                )
        );
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
    public void stopQrScanner() {
        analyzer.clear();
    }
}

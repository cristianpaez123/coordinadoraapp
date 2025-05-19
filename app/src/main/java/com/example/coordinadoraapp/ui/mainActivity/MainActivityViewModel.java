package com.example.coordinadoraapp.ui.mainActivity;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;

import androidx.annotation.StringRes;
import androidx.camera.view.PreviewView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.coordinadoraapp.domain.mainActivity.QrAnalyzerUC;
import com.example.coordinadoraapp.domain.mainActivity.QrResultListener;
import com.example.coordinadoraapp.domain.mainActivity.StartQrScannerUseCase;
import com.example.coordinadoraapp.domain.usecase.LogoutUseCase;
import com.example.coordinadoraapp.domain.usecase.ValidateRawInputUseCase;
import com.example.coordinadoraapp.ui.mapper.LocationUiMapper;
import com.example.coordinadoraapp.ui.model.LocationUi;
import javax.inject.Inject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isQrVisible = new MutableLiveData<>(false);

    private final QrAnalyzerUC analyzer;
    private final StartQrScannerUseCase startScannerUseCase;
    private final MutableLiveData<Boolean> logoutSuccess = new MutableLiveData<>();

    private final MutableLiveData<RawInputUiState> _rawInputUiState = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final LogoutUseCase logoutUseCase;
    private final ValidateRawInputUseCase validateRawInputUseCase;


    public LiveData<Boolean> getIsQrVisible() {
        return isQrVisible;
    }

    @Inject
    public MainActivityViewModel(LogoutUseCase logoutUseCase, ValidateRawInputUseCase validateRawInputUseCase, QrAnalyzerUC analyzer, StartQrScannerUseCase startScannerUseCase) {
        this.logoutUseCase = logoutUseCase;
        this.validateRawInputUseCase = validateRawInputUseCase;
        this.analyzer = analyzer;
        this.startScannerUseCase = startScannerUseCase;
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
                throwable -> {_rawInputUiState.setValue(new RawInputUiState.Error(throwable.getMessage()));
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
        public static class Loading extends RawInputUiState {
        }

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
    public void stopQrScanner() {
        analyzer.clear();
    }
}


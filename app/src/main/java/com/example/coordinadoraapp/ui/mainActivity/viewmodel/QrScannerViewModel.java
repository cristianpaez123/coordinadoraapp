package com.example.coordinadoraapp.ui.mainActivity.viewmodel;

import android.content.Context;
import android.graphics.RectF;

import androidx.camera.view.PreviewView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coordinadoraapp.domain.usecase.QrAnalyzerUseCase;
import com.example.coordinadoraapp.domain.QrResultListener;
import com.example.coordinadoraapp.domain.usecase.StartQrScannerUseCase;

import javax.inject.Inject;

public class QrScannerViewModel extends ViewModel {

    private final QrAnalyzerUseCase analyzer;
    private final StartQrScannerUseCase startScannerUseCase;

    private final MutableLiveData<Boolean> _isQrVisible = new MutableLiveData<>(false);
    public final LiveData<Boolean> isQrVisible = _isQrVisible;

    private final MutableLiveData<Boolean> _stopCamera = new MutableLiveData<>();
    public final LiveData<Boolean> stopCamera = _stopCamera;

    @Inject
    public QrScannerViewModel(QrAnalyzerUseCase analyzer, StartQrScannerUseCase startScannerUseCase) {
        this.analyzer = analyzer;
        this.startScannerUseCase = startScannerUseCase;
    }

    public void initQrScanner(Context context, LifecycleOwner owner, PreviewView previewView, RectF guideRect, QrResultListener listener) {
        analyzer.setGuideRect(guideRect);
        analyzer.setListener(listener);
        startScannerUseCase.execute(context, owner, previewView, analyzer);
    }

    public void notifyQrVisible(boolean isVisible) {
        _isQrVisible.postValue(isVisible);
    }

    public void triggerStopCamera() {
        _stopCamera.setValue(true);
    }

    public void resetStopCameraFlag() {
        _stopCamera.setValue(false);
    }

    public void stopCamera() {
        startScannerUseCase.stopCamera();
    }

    public void stopQrScanner() {
        analyzer.clear();
    }
}

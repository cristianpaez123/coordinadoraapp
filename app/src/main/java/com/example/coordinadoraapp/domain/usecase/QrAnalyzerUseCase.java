package com.example.coordinadoraapp.domain.usecase;

import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;

import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.example.coordinadoraapp.domain.repository.MainRepository;
import com.example.coordinadoraapp.domain.QrResultListener;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QrAnalyzerUseCase implements ImageAnalysis.Analyzer {

    private final MainRepository repository;
    private RectF guideRect;
    private QrResultListener listener;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public QrAnalyzerUseCase(MainRepository repository) {
        this.repository = repository;
    }

    public void setGuideRect(RectF guideRect) {
        this.guideRect = guideRect;
    }

    public void setListener(QrResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

            disposables.add(repository.processImage(image)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(imageProxy::close)
                    .subscribe(barcodes -> {
                        boolean found = false;
                        for (Barcode barcode : barcodes) {
                            Rect bounds = barcode.getBoundingBox();
                            if (bounds != null && isInsideGuide(bounds)) {
                                found = true;
                                if (listener != null) listener.onQrDetected(barcode.getRawValue());
                                break;
                            }
                        }
                        if (!found && listener != null) listener.onQrNotDetected();
                    }, throwable -> {
                        if (listener != null) listener.onQrNotDetected();
                    }));
        } else {
            imageProxy.close();
        }
    }

    private boolean isInsideGuide(Rect qrBounds) {
        RectF qrRectF = new RectF(qrBounds);
        float tolerance = 50f;
        return guideRect.contains(
                qrRectF.left + tolerance,
                qrRectF.top + tolerance,
                qrRectF.right - tolerance,
                qrRectF.bottom - tolerance
        );
    }

    public void clear() {
        disposables.clear();
    }
}
package com.example.coordinadoraapp.domain.mainActivity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;
import android.util.Log;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import com.example.coordinadoraapp.ui.mainActivity.MainActivityViewModel;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import javax.inject.Inject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QrAnalyzerUseCase implements ImageAnalysis.Analyzer {

    private final MainActivityRepository repository;
    private final MainActivityViewModel viewModel;
    private RectF guideRect;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public QrAnalyzerUseCase(MainActivityRepository repository,
                             MainActivityViewModel viewModel) {
        this.repository = repository;
        this.viewModel = viewModel;
    }

    public void setGuideRect(RectF guideRect) {
        this.guideRect = guideRect;
    }

    @SuppressLint("CheckResult")
    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(
                    mediaImage, imageProxy.getImageInfo().getRotationDegrees()
            );

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
                                viewModel.onQrVisible(barcode.getRawValue());
                                break;
                            }
                        }
                        if (!found) {
                            viewModel.onQrNotVisible();
                        }
                    }, throwable -> Log.e("QrAnalyzer", "Error al procesar el código QR", throwable)));
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


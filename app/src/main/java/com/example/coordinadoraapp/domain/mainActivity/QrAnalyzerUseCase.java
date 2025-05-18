package com.example.coordinadoraapp.domain.mainActivity;

import android.media.Image;

import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.Executor;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QrAnalyzerUseCase implements ImageAnalysis.Analyzer {
    private final MainActivityRepository repository;
    private final Executor executor;

    public QrAnalyzerUseCase(MainActivityRepository repository, Executor executor) {
        this.repository = repository;
        this.executor = executor;
    }

    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

            repository.processImage(image)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(imageProxy::close)
                    .subscribe(
                            result -> {
                                // handle success (emit event to ViewModel maybe)
                            },
                            throwable -> {
                                // handle error
                            }
                    );
        } else {
            imageProxy.close();
        }
    }
}


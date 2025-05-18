package com.example.coordinadoraapp.domain.mainActivity;

import com.google.mlkit.vision.common.InputImage;

import io.reactivex.rxjava3.core.Single;

public interface MainActivityRepository {
    Single<String> processImage(InputImage image);

}

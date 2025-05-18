package com.example.coordinadoraapp.data.repository;

import com.example.coordinadoraapp.domain.mainActivity.MainActivityRepository;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class MainActivityImpl implements MainActivityRepository {

    private final BarcodeScanner barcodeScanner;

    @Inject
    public MainActivityImpl(BarcodeScanner barcodeScanner) {
        this.barcodeScanner = barcodeScanner;
    }

    @Override
    public Single<String> processImage(InputImage image) {
        return Single.create(emitter ->
                barcodeScanner.process(image)
                        .addOnSuccessListener(barcodes -> {
                            for (Barcode barcode : barcodes) {
                                if (!emitter.isDisposed()) {
                                    emitter.onSuccess(barcode.getRawValue());
                                    return;
                                }
                            }
                            if (!emitter.isDisposed()) emitter.onError(new Exception("No barcode found"));
                        })
                        .addOnFailureListener(emitter::onError)
        );
    }
}

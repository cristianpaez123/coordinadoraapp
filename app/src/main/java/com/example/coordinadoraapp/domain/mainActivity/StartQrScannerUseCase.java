package com.example.coordinadoraapp.domain.mainActivity;

import android.content.Context;
import android.util.Log;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

public class StartQrScannerUseCase {

    private ProcessCameraProvider cameraProvider;

    public void execute(
            Context context,
            LifecycleOwner lifecycleOwner,
            PreviewView previewView,
            QrAnalyzerUC analyzer
    ) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer);

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis);

            } catch (Exception e) {
                Log.e("StartCamera", "Error iniciando cámara", e);
            }
        }, ContextCompat.getMainExecutor(context));
    }
    public void stopCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }
}

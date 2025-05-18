package com.example.coordinadoraapp.ui.mainActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;
import com.example.coordinadoraapp.MyApplication;
import com.example.coordinadoraapp.R;
import com.example.coordinadoraapp.domain.mainActivity.MainActivityRepository;
import com.example.coordinadoraapp.domain.mainActivity.QrAnalyzerUseCase;
import com.example.coordinadoraapp.utils.QrOverlay;
import com.example.coordinadoraapp.databinding.ActivityLoginBinding;
import com.example.coordinadoraapp.databinding.ActivityMainBinding;
import com.example.coordinadoraapp.ui.login.LoginActivity;
import com.google.common.util.concurrent.ListenableFuture;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    MainActivityRepository repository;
    @Inject
    QrAnalyzerUseCase qrAnalyzerUseCase;
    private MainActivityViewModel viewModel;
    private ImageView cameraIcon;
    private PreviewView previewView;
    private boolean guideRectReady = false;
    private boolean cameraStarted = false;

    private ActivityMainBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setupViewBinding();
        viewModel = new ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel.class);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nuevomain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cameraIcon = findViewById(R.id.cameraIcon);
        previewView = findViewById(R.id.previewView);
        QrOverlay qrOverlay = findViewById(R.id.qrOverlay);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel.class);

        qrOverlay.post(() -> {
            RectF guideRect = qrOverlay.getGuideRect();
            qrAnalyzerUseCase.setGuideRect(guideRect);
            guideRectReady = true;

            if (cameraStarted) {
                startCamera();
            }
        });
        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 1001);
                } else {
                    cameraStarted = true;
                    if (guideRectReady) {
                        startCamera();
                    }
                }
            }
        });
        viewModel.getIsQrVisible().observe(this, isVisible -> {
            qrOverlay.setBorderColor(isVisible ? Color.GREEN : Color.WHITE);
        });

        binding.btnLogin.setOnClickListener(v -> viewModel.logout());

        viewModel.getLogoutSuccess().observe(this, success -> {
            if (success != null && success) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
    }

    private void setupViewBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void startCamera() {
        previewView.setVisibility(View.VISIBLE);
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), qrAnalyzerUseCase);

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
            } catch (Exception e) {
                Log.e("CameraX", "Error: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        qrAnalyzerUseCase.clear();
    }
}
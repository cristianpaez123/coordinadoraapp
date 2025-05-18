package com.example.coordinadoraapp.ui.mainActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
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
import com.example.coordinadoraapp.databinding.ActivityLoginBinding;
import com.example.coordinadoraapp.databinding.ActivityMainBinding;
import com.example.coordinadoraapp.ui.login.LoginActivity;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private MainActivityViewModel viewModel;
    private ImageView cameraIcon;
    private PreviewView previewView;

    private ActivityMainBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getAppComponent().inject(this);
        setupViewBinding();
        viewModel = new ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel.class);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nuevomain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cameraIcon = findViewById(R.id.cameraIcon);
        previewView = findViewById(R.id.previewView);

        cameraIcon.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA}, 1001);
            } else {
                startCamera();
            }
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

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
                    Image mediaImage = imageProxy.getImage();
                    if (mediaImage != null) {
                        InputImage image = InputImage.fromMediaImage(
                                mediaImage,
                                imageProxy.getImageInfo().getRotationDegrees()
                        );
                        viewModel.processImage(image);
                    }
                    imageProxy.close();
                });

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
            } catch (Exception e) {
                Log.e("CameraX", "Error: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));
    }
}
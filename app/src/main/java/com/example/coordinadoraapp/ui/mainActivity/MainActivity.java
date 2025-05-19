package com.example.coordinadoraapp.ui.mainActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;
import android.widget.Toast;
import com.example.coordinadoraapp.MyApplication;
import com.example.coordinadoraapp.R;
import com.example.coordinadoraapp.domain.mainActivity.MainActivityRepository;
import com.example.coordinadoraapp.utils.CameraPermissionHelper;
import com.example.coordinadoraapp.utils.QrOverlay;
import com.example.coordinadoraapp.databinding.ActivityLoginBinding;
import com.example.coordinadoraapp.databinding.ActivityMainBinding;
import com.example.coordinadoraapp.ui.login.LoginActivity;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    MainActivityRepository repository;
    private MainActivityViewModel viewModel;
    private ImageView cameraIcon;
    private PreviewView previewView;
    private QrOverlay qrOverlay;

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
        qrOverlay = findViewById(R.id.qrOverlay);

        cameraIcon.setOnClickListener(v -> {
            if (CameraPermissionHelper.hasCameraPermission(this)) {
                startScannerAfterLayout();
            } else {
                if (CameraPermissionHelper.shouldShowRationale(this)) {
                }
                CameraPermissionHelper.requestCameraPermission(this);
            }
        });
        viewModel.getIsQrVisible().observe(this, isVisible -> {
            qrOverlay.setBorderColor(isVisible ? Color.GREEN : Color.WHITE);
        });



        binding.btnLogin.setOnClickListener(v -> viewModel.logout());

        binding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.submitEncodedText(v.getText().toString());
                return true;
            }
            return false;
        });

        viewModel.getLogoutSuccess().observe(this, success -> {
            if (success != null && success) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
    }

    private void startScannerAfterLayout() {
        qrOverlay.post(() -> {
            RectF guideRect = qrOverlay.getGuideRect();
            viewModel.initQrScanner(this, this, previewView, guideRect);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CameraPermissionHelper.REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScannerAfterLayout();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    openAppSettings();
                } else {
                    Toast.makeText(this, "Permiso de cámara requerido para escanear", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void setupViewBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
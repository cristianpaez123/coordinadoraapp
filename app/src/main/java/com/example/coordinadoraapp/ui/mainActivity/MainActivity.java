package com.example.coordinadoraapp.ui.mainActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.coordinadoraapp.utils.QrOverlay;
import com.example.coordinadoraapp.databinding.ActivityLoginBinding;
import com.example.coordinadoraapp.databinding.ActivityMainBinding;
import com.example.coordinadoraapp.ui.login.LoginActivity;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    MainActivityRepository repository;
    private MainActivityViewModel viewModel;
    private ImageView cameraIcon;
    private PreviewView previewView;

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

        cameraIcon.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, 1001);
            } else {
                qrOverlay.post(() -> {
                    RectF guideRect = qrOverlay.getGuideRect();
                    viewModel.initQrScanner(this, this, previewView, guideRect);
                });
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

    private void setupViewBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
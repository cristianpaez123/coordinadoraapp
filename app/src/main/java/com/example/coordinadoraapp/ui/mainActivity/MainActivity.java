package com.example.coordinadoraapp.ui.mainActivity;

import static com.example.coordinadoraapp.utils.CameraPermissionManager.REQUEST_CODE_CAMERA;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coordinadoraapp.MyApplication;
import com.example.coordinadoraapp.R;
import com.example.coordinadoraapp.databinding.ActivityMainBinding;
import com.example.coordinadoraapp.domain.mainActivity.MainActivityRepository;
import com.example.coordinadoraapp.ui.Map.MapFragment;
import com.example.coordinadoraapp.sync.LocationSyncManager;
import com.example.coordinadoraapp.ui.login.LoginActivity;
import com.example.coordinadoraapp.ui.mainActivity.adapter.LocationAdapter;
import com.example.coordinadoraapp.ui.mainActivity.state.LocationsUiState;
import com.example.coordinadoraapp.ui.mainActivity.state.RawInputUiState;
import com.example.coordinadoraapp.ui.model.LocationUi;
import com.example.coordinadoraapp.utils.CameraPermissionManager;
import com.example.coordinadoraapp.utils.QrOverlay;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;

public class MainActivity extends AppCompatActivity implements LocationAdapter.OnMapClickListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    LocationSyncManager locationSyncManager;
    @Inject
    MainActivityRepository repository;
    private MainActivityViewModel viewModel;
    private ImageView cameraIcon;
    private PreviewView previewView;
    private ImageView btnCloseCamera;

    private QrOverlay qrOverlay;
    private CameraPermissionManager permissionManager;

    private ActivityMainBinding binding;

    LocationAdapter adapter = new LocationAdapter(this);

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel.class);
        permissionManager = new CameraPermissionManager(this, REQUEST_CODE_CAMERA);
        setupViewBinding();
        ViewCompat.setOnApplyWindowInsetsListener(binding.nuevomain, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupUI();
        observeViewModel();
        closeCamera();
    }

    private void setupViewBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void setupUI() {
        cameraIcon = findViewById(R.id.cameraIcon);
        previewView = findViewById(R.id.previewView);
        qrOverlay = findViewById(R.id.qrOverlay);
        btnCloseCamera = findViewById(R.id.btnCloseCamera);

        cameraIcon.setOnClickListener(v -> handleCameraPermission());

        binding.btnLogin.setOnClickListener(v -> viewModel.logout());

        binding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.submitEncodedText(v.getText().toString());
                return true;
            }
            return false;
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

    }

    private void handleCameraPermission() {
        if (permissionManager.hasPermission()) {
            showCamera();
        } else {
            if (permissionManager.shouldShowRationale()) {
                Toast.makeText(this, "Necesitamos permiso de cámara para escanear el código QR", Toast.LENGTH_SHORT).show();
            }
            permissionManager.requestPermission();
        }
    }

    private void openMapFragment(String latitude, String longitude) {
        MapFragment fragment = new MapFragment();

        Bundle args = new Bundle();
        args.putString("lat", latitude);
        args.putString("lng", longitude);
        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void observeViewModel() {
        viewModel.isQrVisible.observe(this, isVisible -> {
            qrOverlay.setBorderColor(isVisible ? Color.GREEN : Color.WHITE);
        });

        viewModel.stopCamera.observe(this, stop -> {
            if (stop != null && stop) {
                viewModel.stopCamera(); // ← detiene CameraX desde el UseCase
                binding.previewView.setVisibility(View.GONE);
                viewModel.resetStopCameraFlag(); // ← limpia el LiveData
            }
        });

        viewModel.getLogoutSuccess().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });

        viewModel.locationsState.observe(this, state -> {
            if (state instanceof LocationsUiState.Success) {
                adapter.updateItems(((LocationsUiState.Success) state).data);
            }
        });

        viewModel.rawInputUiState.observe(this, state -> {
            if (state instanceof RawInputUiState.Success) {
                LocationUi location = ((RawInputUiState.Success) state).locationUi;
                adapter.addItemAtTop(location);
                binding.recyclerView.scrollToPosition(0);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCamera();
            } else if (!permissionManager.shouldShowRationale()) {
                permissionManager.openAppSettings();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showCamera() {
        previewView.setVisibility(View.VISIBLE);
        btnCloseCamera.setVisibility(View.VISIBLE);
        qrOverlay.post(() -> {
            RectF guideRect = qrOverlay.getGuideRect();
            viewModel.initQrScanner(this, this, previewView, guideRect);
        });
    }

    private void closeCamera() {
        btnCloseCamera.setOnClickListener(v -> {
            previewView.setVisibility(View.GONE);
            btnCloseCamera.setVisibility(View.GONE);
            viewModel.stopQrScanner();
        });
    }

    @Override
    public void onMapClick(String latitude, String longitude) {
        openMapFragment(latitude, longitude);
    }
}
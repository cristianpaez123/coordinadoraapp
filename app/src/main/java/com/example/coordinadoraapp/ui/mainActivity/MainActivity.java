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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coordinadoraapp.MyApplication;
import com.example.coordinadoraapp.R;
import com.example.coordinadoraapp.databinding.ActivityMainBinding;
import com.example.coordinadoraapp.domain.QrResultListener;
import com.example.coordinadoraapp.domain.repository.MainRepository;
import com.example.coordinadoraapp.ui.Map.MapFragment;
import com.example.coordinadoraapp.sync.LocationSyncManager;
import com.example.coordinadoraapp.ui.login.LoginActivity;
import com.example.coordinadoraapp.ui.mainActivity.adapter.LocationAdapter;
import com.example.coordinadoraapp.ui.mainActivity.state.LocationsUiState;
import com.example.coordinadoraapp.ui.mainActivity.state.RawInputUiState;
import com.example.coordinadoraapp.ui.mainActivity.viewmodel.QrScannerViewModel;
import com.example.coordinadoraapp.ui.mainActivity.viewmodel.LocationViewModel;
import com.example.coordinadoraapp.ui.mainActivity.viewmodel.SessionViewModel;
import com.example.coordinadoraapp.ui.model.LocationUi;
import com.example.coordinadoraapp.utils.CameraPermissionManager;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;

public class MainActivity extends AppCompatActivity implements LocationAdapter.OnMapClickListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    LocationSyncManager locationSyncManager;
    @Inject
    MainRepository repository;

    private QrScannerViewModel qrScannerViewModel;
    private LocationViewModel locationViewModel;
    private SessionViewModel sessionViewModel;

    private CameraPermissionManager permissionManager;

    private ActivityMainBinding binding;

    LocationAdapter adapter = new LocationAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);

        qrScannerViewModel = new ViewModelProvider(this, viewModelFactory).get(QrScannerViewModel.class);
        locationViewModel = new ViewModelProvider(this, viewModelFactory).get(LocationViewModel.class);
        sessionViewModel = new ViewModelProvider(this, viewModelFactory).get(SessionViewModel.class);

        permissionManager = new CameraPermissionManager(this, REQUEST_CODE_CAMERA);

        setupViewBinding();
        ViewCompat.setOnApplyWindowInsetsListener(binding.activityLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupUI();
        observeViewModels();
        closeCamera();
    }

    private void setupViewBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void setupUI() {
        binding.cameraIcon.setOnClickListener(v -> handleCameraPermission());
        binding.btnLogin.setOnClickListener(v -> sessionViewModel.logout());

        binding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                locationViewModel.submit(v.getText().toString());
                return true;
            }
            return false;
        });

        binding.btnConfirm.setOnClickListener(v -> locationViewModel.submit(binding.editText.getText().toString()));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void handleCameraPermission() {
        if (permissionManager.hasPermission()) {
            showCamera();
        } else {
            if (permissionManager.shouldShowRationale()) {
                Toast.makeText(this, getString(R.string.camera_permission_rationale), Toast.LENGTH_SHORT).show();
            }
            permissionManager.requestPermission();
        }
    }

    private void showCamera() {
        binding.previewView.setVisibility(View.VISIBLE);
        binding.btnCloseCamera.setVisibility(View.VISIBLE);
        binding.qrOverlay.post(() -> {
            RectF guideRect = binding.qrOverlay.getGuideRect();
            qrScannerViewModel.initQrScanner(this, this, binding.previewView, guideRect, new QrResultListener() {
                @Override
                public void onQrDetected(String value) {
                    locationViewModel.submit(value);
                    qrScannerViewModel.notifyQrVisible(true);
                    qrScannerViewModel.triggerStopCamera();
                }

                @Override
                public void onQrNotDetected() {
                    qrScannerViewModel.notifyQrVisible(false);
                }
            });
        });
    }

    private void closeCamera() {
        binding.btnCloseCamera.setOnClickListener(v -> {
            binding.previewView.setVisibility(View.GONE);
            binding.btnCloseCamera.setVisibility(View.GONE);
            qrScannerViewModel.stopQrScanner();
        });
    }

    private void observeViewModels() {
        qrScannerViewModel.isQrVisible.observe(this, isVisible -> binding.qrOverlay.setBorderColor(isVisible ? Color.GREEN : Color.WHITE));

        qrScannerViewModel.stopCamera.observe(this, stop -> {
            if (stop != null && stop) {
                qrScannerViewModel.stopCamera();
                binding.previewView.setVisibility(View.GONE);
                qrScannerViewModel.resetStopCameraFlag();
            }
        });

        sessionViewModel.logoutSuccess.observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });

        locationViewModel.getLocationsState.observe(this, state -> {
            if (state instanceof LocationsUiState.Success) {
                adapter.updateItems(((LocationsUiState.Success) state).data);
            }
        });

        locationViewModel.rawInputUiState.observe(this, state -> {
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
                Toast.makeText(this, getString(R.string.camera_permission_denied), Toast.LENGTH_LONG).show();
            }
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

    @Override
    public void onMapClick(String latitude, String longitude) {
        openMapFragment(latitude, longitude);
    }
}
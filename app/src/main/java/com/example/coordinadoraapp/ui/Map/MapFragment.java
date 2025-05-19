package com.example.coordinadoraapp.ui.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.coordinadoraapp.MyApplication;
import com.example.coordinadoraapp.R;
import com.example.coordinadoraapp.utils.UbicationPermissionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1002;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private MapViewModel viewModel;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;

    private LatLng destination;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) requireActivity().getApplication()).getAppComponent().inject(this);

        if (getArguments() != null) {
            double lat = getArguments().getDouble("lat", 0.0);
            double lng = getArguments().getDouble("lng", 0.0);
            destination = new LatLng(lat, lng);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(MapViewModel.class);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        observePolyline();
        requestUserLocation();

    }

    private void observePolyline() {
        viewModel.getPolylineObservable()
                .observe(getViewLifecycleOwner(), polyline -> {
                    if (googleMap == null || polyline == null) return;

                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions().position(polyline.getPointA()).title("Punto A"));
                    googleMap.addMarker(new MarkerOptions().position(polyline.getPointB()).title("Punto B"));
                    googleMap.addPolyline(polyline.getOptions());

                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(polyline.getPointA())
                            .include(polyline.getPointB())
                            .build();
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                });
    }

    private void requestUserLocation() {
        if (!UbicationPermissionManager.hasLocationPermission(requireContext())) {
            UbicationPermissionManager.requestLocationPermission(this, REQUEST_LOCATION_PERMISSION);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location == null) {
                        Log.w("MapFragment", "Ubicación actual es null.");
                        return;
                    }

                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    viewModel.loadPolyline(userLocation, destination);
                })
                .addOnFailureListener(e ->
                        Log.e("MapFragment", "Error al obtener ubicación", e));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (UbicationPermissionManager.isLocationPermissionGranted(requestCode, permissions, grantResults)) {
            requestUserLocation();
        } else {
            Toast.makeText(getContext(), "Se requiere permiso de ubicación", Toast.LENGTH_SHORT).show();
        }
    }
}

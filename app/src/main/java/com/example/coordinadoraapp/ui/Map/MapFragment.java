package com.example.coordinadoraapp.ui.Map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.coordinadoraapp.MyApplication;
import com.example.coordinadoraapp.R;
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

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1002;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private MapViewModel viewModel;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;

    private double destinationLat = 0.0;
    private double destinationLng = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) requireActivity().getApplication()).getAppComponent().inject(this);

        if (getArguments() != null) {
            destinationLat = getArguments().getDouble("lat", 0.0);
            destinationLng = getArguments().getDouble("lng", 0.0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        viewModel = new ViewModelProvider(this, viewModelFactory).get(MapViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        observePolyline();
        getUserLocationIfPermitted();

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

    private void getUserLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            LatLng destination = new LatLng(destinationLat, destinationLng);

                            viewModel.loadPolyline(userLocation, destination);
                        } else {
                            Log.w("MapFragment", "Ubicación actual es null.");
                        }
                    })
                    .addOnFailureListener(e ->
                            Log.e("MapFragment", "Error al obtener ubicación", e));
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION
            );
        }
    }
}
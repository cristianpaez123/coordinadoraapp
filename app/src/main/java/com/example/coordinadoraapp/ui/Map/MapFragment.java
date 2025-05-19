package com.example.coordinadoraapp.ui.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.coordinadoraapp.MyApplication;
import com.example.coordinadoraapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private MapViewModel viewModel;
    private GoogleMap googleMap;

    private SupportMapFragment mapFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MyApplication) requireActivity().getApplication()).getAppComponent().inject(this);
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        viewModel = new ViewModelProvider(this, viewModelFactory).get(MapViewModel.class);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;
        viewModel.getPolylineObservable()
                .observe(getViewLifecycleOwner(), polyline -> {
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

        viewModel.loadPolyline(); // dispara el caso de uso
    }
}

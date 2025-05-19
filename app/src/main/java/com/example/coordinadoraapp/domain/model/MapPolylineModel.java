package com.example.coordinadoraapp.domain.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapPolylineModel {
    private final LatLng pointA;
    private final LatLng pointB;
    private final PolylineOptions options;

    public MapPolylineModel(LatLng pointA, LatLng pointB, PolylineOptions options) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.options = options;
    }

    public LatLng getPointA() { return pointA; }
    public LatLng getPointB() { return pointB; }
    public PolylineOptions getOptions() { return options; }
}

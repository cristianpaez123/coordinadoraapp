package com.example.coordinadoraapp.ui.model;

public class LocationUi {
    public final String label;
    public final String latitude;
    public final String longitude;
    public final String observation;

    public LocationUi(String label, String latitude, String longitude, String observation) {
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
        this.observation = observation;
    }
}

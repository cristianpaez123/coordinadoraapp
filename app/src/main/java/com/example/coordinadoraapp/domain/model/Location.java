package com.example.coordinadoraapp.domain.model;

public class Location {
    public final String label;
    public final String latitude;
    public final String longitude;
    public final String observation;

    public Location(String label, String latitude, String longitude, String observation) {
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
        this.observation = observation;
    }
}
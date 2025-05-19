package com.example.coordinadoraapp.domain.model;

public class Location {
    public String label;
    public String latitude;
    public String longitude;
    public String observation;

    public Location() {}

    public Location(String label, String latitude, String longitude, String observation) {
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
        this.observation = observation;
    }
}
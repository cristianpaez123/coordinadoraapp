package com.example.coordinadoraapp.ui.mapper;

import com.example.coordinadoraapp.domain.model.Location;
import com.example.coordinadoraapp.ui.model.LocationUi;

public class LocationUiMapper {

    public static LocationUi toUi(Location location) {
        return new LocationUi(
            location.label,
            location.latitude,
            location.longitude,
            location.observation
        );
    }

    public static Location toDomain(LocationUi ui) {
        return new Location(
            ui.label,
            ui.latitude,
            ui.longitude,
            ui.observation
        );
    }
}

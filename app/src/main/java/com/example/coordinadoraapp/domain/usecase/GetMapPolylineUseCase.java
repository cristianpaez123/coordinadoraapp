package com.example.coordinadoraapp.domain.usecase;

import android.graphics.Color;

import com.example.coordinadoraapp.domain.model.MapPolylineModel;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;

import io.reactivex.rxjava3.core.Single;

public class GetMapPolylineUseCase {

    public Single<MapPolylineModel> execute() {
        return Single.fromCallable(() -> {
            LatLng pointA = new LatLng(4.6483, -74.2479); // Bogotá
            LatLng pointB = new LatLng(4.7110, -74.0721); // otro punto

            PolylineOptions options = new PolylineOptions()
                    .add(pointA)
                    .add(pointB)
                    .color(Color.BLUE)
                    .width(6)
                    .pattern(Arrays.asList(new Dot(), new Gap(10)));

            return new MapPolylineModel(pointA, pointB, options);
        });
    }
}

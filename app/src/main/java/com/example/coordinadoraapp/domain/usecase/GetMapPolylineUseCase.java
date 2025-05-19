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

    public Single<MapPolylineModel> execute(LatLng pointA, LatLng pointB) {
        return Single.fromCallable(() -> {
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

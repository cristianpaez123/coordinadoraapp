package com.example.coordinadoraapp.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import io.reactivex.rxjava3.annotations.NonNull;

public class UbicationPermissionManager {
    public static boolean hasLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestLocationPermission(Fragment fragment, int requestCode) {
        fragment.requestPermissions(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                requestCode
        );
    }

    public static boolean isLocationPermissionGranted(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 1002) return false;
        if (permissions.length == 0 || grantResults.length == 0) return false;
        return grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}

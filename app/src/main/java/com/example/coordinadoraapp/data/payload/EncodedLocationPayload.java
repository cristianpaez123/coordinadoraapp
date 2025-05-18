package com.example.coordinadoraapp.data.payload;

import android.util.Base64;

public class EncodedLocationPayload {
    public final String data;

    public EncodedLocationPayload(String base64) {
        this.data = "[" + base64 + "]";
    }
}


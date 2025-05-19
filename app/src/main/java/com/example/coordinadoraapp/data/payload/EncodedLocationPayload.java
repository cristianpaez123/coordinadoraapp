package com.example.coordinadoraapp.data.payload;

public class EncodedLocationPayload {
    public final String data;

    public EncodedLocationPayload(String base64) {
        this.data = "[" + base64 + "]";
    }
}


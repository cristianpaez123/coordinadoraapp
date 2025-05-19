package com.example.coordinadoraapp.domain.mainActivity;

public interface QrResultListener {
    void onQrDetected(String value);
    void onQrNotDetected();
}
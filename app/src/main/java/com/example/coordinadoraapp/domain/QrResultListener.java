package com.example.coordinadoraapp.domain;

public interface QrResultListener {
    void onQrDetected(String value);
    void onQrNotDetected();
}
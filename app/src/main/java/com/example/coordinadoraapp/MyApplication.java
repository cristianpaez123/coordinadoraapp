package com.example.coordinadoraapp;

import android.app.Application;

import com.example.coordinadoraapp.di.AppComponent;
import com.example.coordinadoraapp.di.AppModule;
import com.example.coordinadoraapp.di.DaggerAppComponent;

public class MyApplication extends Application {
    public AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}

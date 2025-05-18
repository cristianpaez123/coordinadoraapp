package com.example.coordinadoraapp;

import android.app.Application;

import com.example.coordinadoraapp.di.AppComponent;
import com.example.coordinadoraapp.di.AppModule;
import com.example.coordinadoraapp.di.DaggerAppComponent;
import com.example.coordinadoraapp.di.NetworkModule;

public class MyApplication extends Application {
    public static AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}

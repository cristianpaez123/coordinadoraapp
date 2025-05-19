package com.example.coordinadoraapp;

import android.app.Application;

import com.example.coordinadoraapp.di.AppComponent;
import com.example.coordinadoraapp.di.AppModule;
import com.example.coordinadoraapp.di.DaggerAppComponent;

public class MyApplication extends Application {
    public static AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}

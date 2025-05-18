package com.example.coordinadoraapp.di;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    public RequestQueue provideRequestQueue(Context context) {
        return Volley.newRequestQueue(context.getApplicationContext());
    }
}

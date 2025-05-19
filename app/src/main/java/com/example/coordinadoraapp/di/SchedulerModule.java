package com.example.coordinadoraapp.di;

import com.example.coordinadoraapp.di.anotation.IoScheduler;
import com.example.coordinadoraapp.di.anotation.MainScheduler;

import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Module
public class SchedulerModule {

    @Provides
    @IoScheduler
    public Scheduler provideIoScheduler() {
        return Schedulers.io();
    }

    @Provides
    @MainScheduler
    public Scheduler provideMainScheduler() {
        return AndroidSchedulers.mainThread();
    }
}


package com.example.coordinadoraapp.di;

import android.app.Application;

import com.example.coordinadoraapp.ui.Map.MapFragment;
import com.example.coordinadoraapp.MyApplication;
import com.example.coordinadoraapp.ui.launcher.LauncherActivity;
import com.example.coordinadoraapp.ui.mainActivity.MainActivity;
import com.example.coordinadoraapp.di.viewmodel.ViewModelModule;
import com.example.coordinadoraapp.ui.login.LoginActivity;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        UseCaseModule.class,
        RepositoryModule.class,
        ViewModelModule.class,
        NetworkModule.class,
        SchedulerModule.class
})
public interface AppComponent {
    void inject(MyApplication application);
    void inject(MainActivity activity);
    void inject(LoginActivity activity);
    void inject(LauncherActivity activity);
    void inject(MapFragment mapFragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        Builder appModule(AppModule appModule);

        Builder networkModule(NetworkModule networkModule);

        AppComponent build();
    }

}

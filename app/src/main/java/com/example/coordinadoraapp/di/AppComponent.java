package com.example.coordinadoraapp.di;

import android.app.Application;

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
        NetworkModule.class
})
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(LoginActivity activity);
    void inject(LauncherActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        Builder appModule(AppModule appModule);

        AppComponent build();
    }

}

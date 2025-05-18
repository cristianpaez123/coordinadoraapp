package com.example.coordinadoraapp.di;

import android.app.Application;

import com.example.coordinadoraapp.ui.mainActivity.MainActivity;
import com.example.coordinadoraapp.ui.login.LoginActivity;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        FirebaseModule.class,
        ViewModelModule.class
})
public interface AppComponent {
    void inject(MainActivity mainActivity);
    void inject(LoginActivity loginActivity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        Builder appModule(AppModule appModule);

        AppComponent build();
    }

}

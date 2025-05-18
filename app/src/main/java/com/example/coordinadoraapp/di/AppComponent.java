package com.example.coordinadoraapp.di;

import com.example.coordinadoraapp.ui.MainActivity;
import com.example.coordinadoraapp.ui.login.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        UseCaseModule.class,
        RepositoryModule.class
})
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);

}

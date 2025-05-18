package com.example.coordinadoraapp.ui.launcher;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coordinadoraapp.MyApplication;
import com.example.coordinadoraapp.domain.usecase.CheckAuthUseCase;
import com.example.coordinadoraapp.ui.login.LoginActivity;
import com.example.coordinadoraapp.ui.mainActivity.MainActivity;

import javax.inject.Inject;

public class LauncherActivity extends AppCompatActivity {

    @Inject
    CheckAuthUseCase checkAuthUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getApplication()).appComponent.inject(this);

        if (checkAuthUseCase.execute()) {
            navigateTo(MainActivity.class);
        } else {
            navigateTo(LoginActivity.class);
        }
    }

    private void navigateTo(Class<?> target) {
        startActivity(new Intent(this, target));
        finish();
    }
}
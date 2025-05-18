package com.example.coordinadoraapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.coordinadoraapp.MyApplication;
import com.example.coordinadoraapp.R;
import com.example.coordinadoraapp.di.DaggerAppComponent;
import com.example.coordinadoraapp.di.DaggerViewModelFactory;
import com.example.coordinadoraapp.ui.mainActivity.MainActivity;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private LoginViewModel viewModel;

    private EditText emailField, passwordField;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerAppComponent.builder().build().inject(this);
        super.onCreate(savedInstanceState);
        ((MyApplication) getApplication()).appComponent.inject(this);

        setContentView(R.layout.activity_main);
        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.btnLogin);

        viewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            viewModel.login(email, password);
        });

        viewModel.loginSuccess.observe(this, success -> {
            if (success) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });

        viewModel.error.observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
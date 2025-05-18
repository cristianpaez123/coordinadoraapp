package com.example.coordinadoraapp.ui.login;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.coordinadoraapp.MyApplication;
import com.example.coordinadoraapp.databinding.ActivityLoginBinding;
import com.example.coordinadoraapp.ui.mainActivity.MainActivity;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        setupViewBinding();
        setupViewModel();
        setupListeners();
        setupObservers();
    }

    private void injectDependencies() {
        ((MyApplication) getApplication()).appComponent.inject(this);
    }

    private void setupViewBinding() {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString().trim();
            String password = binding.editTextPassword.getText().toString().trim();
            viewModel.login(email, password);
        });
    }

    private void setupObservers() {
        viewModel.uiState.observe(this, state -> {
            loadingState();
            if (state instanceof LoginViewModel.LoginUiState.Loading) {
                loadingState();
            } else if (state instanceof LoginViewModel.LoginUiState.Success) {
                successState();
            } else if (state instanceof LoginViewModel.LoginUiState.Error) {
                errorState(((LoginViewModel.LoginUiState.Error) state).message);
            }
        });
    }

    private void loadingState() {
        binding.progressBar.setVisibility(VISIBLE);
    }

    private void successState() {
        startActivity(new Intent(this, MainActivity.class));
        binding.progressBar.setVisibility(GONE);
        finish();
    }

    private void errorState(String message) {
        binding.progressBar.setVisibility(GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

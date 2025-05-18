package com.example.coordinadoraapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.coordinadoraapp.MyApplication;
import com.example.coordinadoraapp.R;
import com.example.coordinadoraapp.databinding.ActivityLoginBinding;
import com.example.coordinadoraapp.ui.mainActivity.MainActivity;
import com.google.android.material.snackbar.Snackbar;

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
                errorState((LoginViewModel.LoginUiState.Error) state);
            }
        });
    }

    private void loadingState() {
        showLoadingOverlay(true);
    }

    private void successState() {
        showLoadingOverlay(false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void errorState(LoginViewModel.LoginUiState.Error state) {
        showLoadingOverlay(false);
        String message = state.messageRes != null
            ? getString(state.messageRes)
            : (state.message != null ? state.message : getString(R.string.error_unexpected));
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }

    private void showLoadingOverlay(boolean show) {
        binding.includeLoadingOverlay.loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}

package com.example.coordinadoraapp.utils;

import android.util.Patterns;

import androidx.annotation.StringRes;

import com.example.coordinadoraapp.R;

public class CredentialValidator {

    private CredentialValidator() {
        // no instances
    }

    @StringRes
    public static Integer validate(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return R.string.error_email_required;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return R.string.error_email_invalid;
        }

        if (password == null || password.trim().isEmpty()) {
            return R.string.error_password_required;
        }

        return null;
    }
}

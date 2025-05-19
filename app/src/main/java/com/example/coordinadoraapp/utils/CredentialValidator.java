package com.example.coordinadoraapp.utils;

import androidx.annotation.StringRes;

import com.example.coordinadoraapp.R;

import java.util.regex.Pattern;

public class CredentialValidator {

    private CredentialValidator() {
        // no instances
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
    );

    @StringRes
    public static Integer validate(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return R.string.error_email_required;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return R.string.error_email_invalid;
        }

        if (password == null || password.trim().isEmpty()) {
            return R.string.error_password_required;
        }

        return null;
    }
}

package com.example.coordinadoraapp.utils;

import androidx.annotation.StringRes;

import com.example.coordinadoraapp.R;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class FirebaseErrorMapper {

    @StringRes
    public static int map(Throwable throwable) {
        if (throwable instanceof FirebaseAuthInvalidUserException) {
            return R.string.error_user_not_found;
        }

        if (throwable instanceof FirebaseAuthInvalidCredentialsException) {
            return R.string.error_invalid_credentials;
        }

        if (throwable instanceof FirebaseAuthUserCollisionException) {
            return R.string.error_user_already_exists;
        }

        if (throwable instanceof FirebaseNetworkException) {
            return R.string.error_network;
        }

        return R.string.error_unexpected;
    }
}

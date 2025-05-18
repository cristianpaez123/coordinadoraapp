package com.example.coordinadoraapp.domain.repository;

import com.google.firebase.auth.AuthResult;

import io.reactivex.rxjava3.core.Single;

public interface AuthRepository {
    Single<AuthResult> login(String email, String password);
}
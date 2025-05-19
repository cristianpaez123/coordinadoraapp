package com.example.coordinadoraapp.domain.repository;


import com.example.coordinadoraapp.domain.model.Location;

import io.reactivex.rxjava3.core.Completable;

public interface RemoteLocationBackupRepository {
    Completable backupLocation(Location location);
}

package com.example.coordinadoraapp.sync;

import com.example.coordinadoraapp.domain.repository.LocationRepository;
import com.example.coordinadoraapp.domain.repository.RemoteLocationBackupRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LocationSyncManager {

    private static final int LOCATIONS_TO_SYNC = 5;

    private final LocationRepository locationRepository;
    private final RemoteLocationBackupRepository remoteBackupRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public LocationSyncManager(
        LocationRepository locationRepository,
        RemoteLocationBackupRepository remoteBackupRepository
    ) {
        this.locationRepository = locationRepository;
        this.remoteBackupRepository = remoteBackupRepository;
        observeAndSync();
    }

    private void observeAndSync() {
        disposables.add(
            locationRepository.getLocationCountStream()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> {
                    if (count > 0 && count % LOCATIONS_TO_SYNC == 0) {
                        syncAllLocations();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                })
        );
    }

    private void syncAllLocations() {
        disposables.add(
            locationRepository.getAllLocations()
                .map(locations -> {
                    int size = locations.size();
                    return locations.subList(Math.max(size - LOCATIONS_TO_SYNC, 0), size);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMapCompletable(remoteBackupRepository::backupLocations)
                .subscribe(
                    () -> {
                    },
                    throwable -> {
                        throwable.printStackTrace();
                    }
                )
        );
    }
}

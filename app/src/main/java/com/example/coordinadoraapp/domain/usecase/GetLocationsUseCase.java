package com.example.coordinadoraapp.domain.usecase;

import com.example.coordinadoraapp.domain.model.Location;
import com.example.coordinadoraapp.domain.repository.LocationRepository;
import com.example.coordinadoraapp.domain.repository.RemoteLocationBackupRepository;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class GetLocationsUseCase {

    private final LocationRepository locationRepository;
    private final RemoteLocationBackupRepository remoteRepository;

    @Inject
    public GetLocationsUseCase(
        LocationRepository locationRepository,
        RemoteLocationBackupRepository remoteRepository
    ) {
        this.locationRepository = locationRepository;
        this.remoteRepository = remoteRepository;
    }

    public Single<List<Location>> execute() {
        return locationRepository.getAllLocations()
            .flatMapObservable(localList -> localList == null || localList.isEmpty()
                ? Observable.empty()
                : Observable.just(localList))
            .switchIfEmpty(
                remoteRepository.getBackedUpLocations()
                    .toObservable()
                    .flatMap(remoteList ->
                        locationRepository.saveAllLocations(remoteList)
                            .andThen(Observable.just(remoteList))))
            .firstOrError()
            .map(list -> {
                Collections.reverse(list);
                return list;
            });
    }
}

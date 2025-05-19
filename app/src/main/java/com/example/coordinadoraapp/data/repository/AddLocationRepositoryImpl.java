package com.example.coordinadoraapp.data.repository;

import com.example.coordinadoraapp.data.dto.LocationDto;
import com.example.coordinadoraapp.data.mapper.LocationDtoMapper;
import com.example.coordinadoraapp.data.payload.EncodedLocationPayload;
import com.example.coordinadoraapp.data.remote.ApiConstants;
import com.example.coordinadoraapp.data.remote.NetworkRequestService;
import com.example.coordinadoraapp.domain.model.Location;
import com.example.coordinadoraapp.domain.repository.AddLocationRepository;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class AddLocationRepositoryImpl implements AddLocationRepository {

    private final NetworkRequestService networkRequestService;

    @Inject
    public AddLocationRepositoryImpl(NetworkRequestService networkRequestService) {
        this.networkRequestService = networkRequestService;
    }

    @Override
    public Single<Location> addLocation(String base64) {

        EncodedLocationPayload payload = new EncodedLocationPayload(base64);

        return networkRequestService.postJson(ApiConstants.VALIDATE_ENDPOINT, payload)
            .flatMap(response -> {
                try {
                    LocationDto dto = new Gson().fromJson(response.toString(), LocationDto.class);

                    if(dto.isSuccess()) {
                        return Single.just(LocationDtoMapper.toDomain(dto));
                    } else {
                        return Single.error(new IllegalArgumentException("Invalid structure: " + dto.error));
                    }
                } catch (Exception e) {
                    return Single.error(e);
                }
            });
    }

}

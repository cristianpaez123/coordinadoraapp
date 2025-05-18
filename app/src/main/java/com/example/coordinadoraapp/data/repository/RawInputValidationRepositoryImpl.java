package com.example.coordinadoraapp.data.repository;

import android.util.Base64;

import com.example.coordinadoraapp.data.dto.LocationDto;
import com.example.coordinadoraapp.data.mapper.QrResponseMapper;
import com.example.coordinadoraapp.data.payload.EncodedLocationPayload;
import com.example.coordinadoraapp.data.remote.ApiConstants;
import com.example.coordinadoraapp.data.remote.RemoteLocationService;
import com.example.coordinadoraapp.domain.model.Location;
import com.example.coordinadoraapp.domain.repository.RawInputValidationRepository;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class RawInputValidationRepositoryImpl implements RawInputValidationRepository {

    private final RemoteLocationService remoteLocationService;

    @Inject
    public RawInputValidationRepositoryImpl(RemoteLocationService remoteLocationService) {
        this.remoteLocationService = remoteLocationService;
    }

    @Override
    public Single<Location> submitEncodedData(String rawText) {
        String base64 = Base64.encodeToString(rawText.getBytes(), Base64.NO_WRAP);
        EncodedLocationPayload payload = new EncodedLocationPayload(base64);

        String url = ApiConstants.BASE_URL + ApiConstants.VALIDATE_ENDPOINT;

        return remoteLocationService.postJson(url, payload)
            .flatMap(response -> {
                try {
                    LocationDto dto = new LocationDto();
                    dto.Correcto = response.getString("Correcto");
                    dto.data = response.getString("data");

                    if ("estructura Correcta".equalsIgnoreCase(dto.Correcto)) {
                        return Single.just(QrResponseMapper.from(dto));
                    } else {
                        return Single.error(new IllegalArgumentException("Invalid structure: " + dto.data));
                    }
                } catch (Exception e) {
                    return Single.error(e);
                }
            });
    }

}

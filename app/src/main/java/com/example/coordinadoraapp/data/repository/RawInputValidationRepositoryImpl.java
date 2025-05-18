package com.example.coordinadoraapp.data.repository;

import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coordinadoraapp.data.dto.LocationDto;
import com.example.coordinadoraapp.data.mapper.QrResponseMapper;
import com.example.coordinadoraapp.domain.model.Location;
import com.example.coordinadoraapp.domain.repository.RawInputValidationRepository;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class RawInputValidationRepositoryImpl implements RawInputValidationRepository {

    private final RequestQueue requestQueue;
    private static final String URL = "https://noderedtest.coordinadora.com/api/v1/validar";

    @Inject
    public RawInputValidationRepositoryImpl(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public Single<Location> submitEncodedData(String rawText) {
        return Single.create(emitter -> {
            String base64 = Base64.encodeToString(rawText.getBytes(), Base64.NO_WRAP);
            String payload = "[" + base64 + "]";

            JSONObject body = new JSONObject();
            try {
                body.put("data", payload);
            } catch (JSONException e) {
                emitter.onError(e);
                return;
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, body,
                response -> {
                    try {
                        LocationDto dto = new LocationDto();
                        dto.Correcto = response.getString("Correcto");
                        dto.data = response.getString("data");

                        if ("estructura Correcta".equalsIgnoreCase(dto.Correcto)) {
                            emitter.onSuccess(QrResponseMapper.from(dto));
                        } else {
                            emitter.onError(new IllegalArgumentException("Invalid structure: " + dto.data));
                        }
                    } catch (JSONException e) {
                        emitter.onError(e);
                    }
                },
                error -> emitter.onError(new Exception("Network error: " + error.getMessage()))
            );

            requestQueue.add(request);
        });
    }
}
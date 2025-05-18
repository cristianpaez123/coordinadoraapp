package com.example.coordinadoraapp.data.remote;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class RemoteLocationService {

    private final RequestQueue requestQueue;
    private final Gson gson;

    @Inject
    public RemoteLocationService(RequestQueue requestQueue, Gson gson) {
        this.requestQueue = requestQueue;
        this.gson = gson;
    }

    public <T> Single<JSONObject> postJson(String url, T payload) {
        return Single.create(emitter -> {
            try {
                String jsonString = gson.toJson(payload);
                JSONObject body = new JSONObject(jsonString);

                JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    body,
                    emitter::onSuccess,
                    error -> emitter.onError(new Exception("Network error: " + error.getMessage()))
                );
                requestQueue.add(request);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}


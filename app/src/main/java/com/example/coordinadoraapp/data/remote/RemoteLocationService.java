package com.example.coordinadoraapp.data.remote;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class RemoteLocationService {

    private final RequestQueue requestQueue;

    @Inject
    public RemoteLocationService(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public Single<JSONObject> postJson(String url, JSONObject body) {
        return Single.create(emitter -> {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                emitter::onSuccess,
                error -> emitter.onError(new Exception("Network error: " + error.getMessage()))
            );
            requestQueue.add(request);
        });
    }
}

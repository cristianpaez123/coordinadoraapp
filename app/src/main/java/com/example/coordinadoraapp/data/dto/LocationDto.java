package com.example.coordinadoraapp.data.dto;

import com.google.gson.annotations.SerializedName;

public class LocationDto {

    @SerializedName("Incorrecto")
    public String error;

    @SerializedName("Correcto")
    private String success;

    @SerializedName("data")
    public String data;

    public boolean isSuccess() {
        return success!= null && error == null;
    }
}

package com.example.coordinadoraapp.data.mapper;

import com.example.coordinadoraapp.data.dto.LocationDto;
import com.example.coordinadoraapp.domain.model.Location;

public class QrResponseMapper {
    public static Location from(LocationDto dto) {
        String[] parts = dto.data.split("-");

        String etiqueta = "", lat = "", lon = "", obs = "";

        for (String part : parts) {
            if (part.startsWith("etiqueta1d:")) etiqueta = part.replace("etiqueta1d:", "");
            else if (part.startsWith("latitud:")) lat = part.replace("latitud:", "");
            else if (part.startsWith("longitud:")) lon = part.replace("longitud:", "");
            else if (part.startsWith("observacion:")) obs = part.replace("observacion:", "");
        }

        return new Location(etiqueta, lat, lon, obs);
    }
}


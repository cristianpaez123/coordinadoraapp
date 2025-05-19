package com.example.coordinadoraapp.data.mapper;

import com.example.coordinadoraapp.data.dto.LocationDto;
import com.example.coordinadoraapp.domain.model.Location;

public class LocationDtoMapper {

    private static final String LABEL_PREFIX = "etiqueta1d";
    private static final String LATITUDE_PREFIX = "latitud";
    private static final String LONGITUDE_PREFIX = "longitud";
    private static final String OBSERVATION_PREFIX = "observacion";

    public static Location toDomain(LocationDto dto) {
        String regex = String.format("-(?=%s:|%s:|%s:|%s:)",
            LABEL_PREFIX, LATITUDE_PREFIX, LONGITUDE_PREFIX, OBSERVATION_PREFIX);
        String[] parts = dto.data.split(regex);

        String label = null;
        String latitude = null;
        String longitude = null;
        String observation = null;

        for (String part : parts) {
            String[] keyValue = part.split(":", 2);
            if (keyValue.length < 2) continue;

            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            switch (key) {
                case LABEL_PREFIX:
                    label = value;
                    break;
                case LATITUDE_PREFIX:
                    latitude = value;
                    break;
                case LONGITUDE_PREFIX:
                    longitude = value;
                    break;
                case OBSERVATION_PREFIX:
                    observation = value;
                    break;
            }
        }

        return new Location(label, latitude, longitude, observation);
    }
}

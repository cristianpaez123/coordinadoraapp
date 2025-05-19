package com.example.coordinadoraapp.ui.mainActivity.state;

import androidx.annotation.StringRes;

import com.example.coordinadoraapp.ui.model.LocationUi;

import java.util.List;

public abstract class LocationsUiState {

    public static class Loading extends LocationsUiState {
    }

    public static class Success extends LocationsUiState {
        public final List<LocationUi> data;

        public Success(List<LocationUi> data) {
            this.data = data;
        }
    }

    public static class Error extends LocationsUiState {
        @StringRes
        public final Integer messageRes;
        public final String message;

        public Error(@StringRes int messageRes) {
            this.messageRes = messageRes;
            this.message = null;
        }

        public Error(String message) {
            this.message = message;
            this.messageRes = null;
        }
    }
}

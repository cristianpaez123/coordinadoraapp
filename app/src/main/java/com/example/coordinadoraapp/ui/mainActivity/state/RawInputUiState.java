package com.example.coordinadoraapp.ui.mainActivity.state;

import androidx.annotation.StringRes;

import com.example.coordinadoraapp.ui.model.LocationUi;

public  abstract class RawInputUiState {
    public static class Loading extends RawInputUiState {
    }

    public static class Success extends RawInputUiState {
        public final LocationUi locationUi;

        public Success(LocationUi locationUi) {
            this.locationUi = locationUi;
        }
    }

    public static class Error extends RawInputUiState {
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

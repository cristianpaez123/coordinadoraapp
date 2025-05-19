package com.example.coordinadoraapp.ui.mainActivity.viewmodel;

import android.util.Base64;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coordinadoraapp.domain.usecase.AddLocationUseCase;
import com.example.coordinadoraapp.domain.usecase.GetLocationsUseCase;
import com.example.coordinadoraapp.ui.mainActivity.state.LocationsUiState;
import com.example.coordinadoraapp.ui.mainActivity.state.AddLocationUiState;
import com.example.coordinadoraapp.ui.mapper.LocationUiMapper;

import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LocationViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final AddLocationUseCase addLocationUseCase;
    private final GetLocationsUseCase getLocationsUseCase;

    private final MutableLiveData<AddLocationUiState> _addLocationUiState = new MutableLiveData<>();
    public final LiveData<AddLocationUiState> addLocationUiState = _addLocationUiState;

    private final MutableLiveData<LocationsUiState> _getLocationsState = new MutableLiveData<>();
    public final LiveData<LocationsUiState> getLocationsState = _getLocationsState;

    @Inject
    public LocationViewModel(AddLocationUseCase addLocationUseCase, GetLocationsUseCase getLocationsUseCase) {
        this.addLocationUseCase = addLocationUseCase;
        this.getLocationsUseCase = getLocationsUseCase;
        loadLocations();
    }

    public void submit(String rawText) {
        String base64 = Base64.encodeToString(rawText.getBytes(), Base64.NO_WRAP);
        _addLocationUiState.setValue(new AddLocationUiState.Loading());
        disposables.add(addLocationUseCase.execute(base64)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                validatedData -> _addLocationUiState.setValue(new AddLocationUiState.Success(LocationUiMapper.toUi(validatedData))),
                throwable -> _addLocationUiState.setValue(new AddLocationUiState.Error(throwable.getMessage()))
            ));
    }

    private void loadLocations() {
        _getLocationsState.setValue(new LocationsUiState.Loading());
        disposables.add(
            getLocationsUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(locations -> locations.stream()
                    .map(LocationUiMapper::toUi)
                    .collect(Collectors.toList()))
                .subscribe(
                    result -> _getLocationsState.setValue(new LocationsUiState.Success(result)),
                    error -> _getLocationsState.setValue(new LocationsUiState.Error(error.getMessage()))
                )
        );
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}

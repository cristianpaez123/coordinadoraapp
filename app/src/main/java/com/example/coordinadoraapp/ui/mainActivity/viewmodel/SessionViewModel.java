package com.example.coordinadoraapp.ui.mainActivity.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coordinadoraapp.domain.usecase.GetLocationsUseCase;
import com.example.coordinadoraapp.domain.usecase.LogoutUseCase;
import com.example.coordinadoraapp.ui.mainActivity.state.LocationsUiState;
import com.example.coordinadoraapp.ui.mapper.LocationUiMapper;

import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SessionViewModel extends ViewModel {

    private final LogoutUseCase logoutUseCase;
    private final GetLocationsUseCase getLocationsUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Boolean> _logoutSuccess = new MutableLiveData<>();
    public final LiveData<Boolean> logoutSuccess = _logoutSuccess;

    private final MutableLiveData<LocationsUiState> _locationsState = new MutableLiveData<>();
    public final LiveData<LocationsUiState> locationsState = _locationsState;

    @Inject
    public SessionViewModel(LogoutUseCase logoutUseCase, GetLocationsUseCase getLocationsUseCase) {
        this.logoutUseCase = logoutUseCase;
        this.getLocationsUseCase = getLocationsUseCase;
        loadLocations();
    }

    public void logout() {
        disposables.add(
            logoutUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> _logoutSuccess.setValue(true),
                    throwable -> {/* log error */})
        );
    }

    private void loadLocations() {
        _locationsState.setValue(new LocationsUiState.Loading());
        disposables.add(
            getLocationsUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(locations -> locations.stream()
                    .map(LocationUiMapper::toUi)
                    .collect(Collectors.toList()))
                .subscribe(
                    result -> _locationsState.setValue(new LocationsUiState.Success(result)),
                    error -> _locationsState.setValue(new LocationsUiState.Error(error.getMessage()))
                )
        );
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}

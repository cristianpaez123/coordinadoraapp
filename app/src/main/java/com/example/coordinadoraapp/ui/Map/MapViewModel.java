package com.example.coordinadoraapp.ui.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.coordinadoraapp.domain.model.MapPolylineModel;
import com.example.coordinadoraapp.domain.usecase.GetMapPolylineUseCase;
import com.google.android.gms.maps.model.LatLng;
import javax.inject.Inject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MapViewModel extends ViewModel {
    private final GetMapPolylineUseCase useCase;
    private final MutableLiveData<MapPolylineModel> polylineLiveData = new MutableLiveData<>();

    @Inject
    public MapViewModel(GetMapPolylineUseCase useCase) {
        this.useCase = useCase;
    }

    public void loadPolyline(LatLng pointA, LatLng pointB) {
        useCase.execute(pointA, pointB)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(polylineLiveData::postValue, Throwable::printStackTrace);
    }

    public LiveData<MapPolylineModel> getPolylineObservable() {
        return polylineLiveData;
    }
}
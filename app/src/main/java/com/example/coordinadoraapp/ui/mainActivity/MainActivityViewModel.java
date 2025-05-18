package com.example.coordinadoraapp.ui.mainActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coordinadoraapp.domain.mainActivity.MainActivityRepository;
import com.example.coordinadoraapp.domain.usecase.LogoutUseCase;
import com.google.mlkit.vision.common.InputImage;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<String> qrResult = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutSuccess = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MainActivityRepository repository;
    private final LogoutUseCase logoutUseCase;

    @Inject
    public MainActivityViewModel(MainActivityRepository repository, LogoutUseCase logoutUseCase) {
        this.repository = repository;
        this.logoutUseCase = logoutUseCase;
    }

    public LiveData<String> getQrResult() { return qrResult; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getLogoutSuccess() { return logoutSuccess; }

    public void processImage(InputImage image) {
        disposables.add(repository.processImage(image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                result -> qrResult.setValue(result),
                throwable -> error.setValue(throwable.getMessage())
            ));
    }

    public void logout() {
        logoutUseCase.execute();
        logoutSuccess.setValue(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}

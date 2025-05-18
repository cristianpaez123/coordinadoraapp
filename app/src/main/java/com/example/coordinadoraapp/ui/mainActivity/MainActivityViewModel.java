package com.example.coordinadoraapp.ui.mainActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.coordinadoraapp.domain.mainActivity.MainActivityRepository;
import com.example.coordinadoraapp.domain.usecase.LogoutUseCase;
import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isQrVisible = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> logoutSuccess = new MutableLiveData<>();

    private final MainActivityRepository repository;
    private final LogoutUseCase logoutUseCase;

    public LiveData<Boolean> getIsQrVisible() { return isQrVisible; }

    private String lastDetectedQr = null;

    @Inject
    public MainActivityViewModel(MainActivityRepository repository, LogoutUseCase logoutUseCase) {
        this.repository = repository;
        this.logoutUseCase = logoutUseCase;
    }
    public LiveData<Boolean> getLogoutSuccess() { return logoutSuccess; }


    public void onQrVisible(String qrValue) {
        lastDetectedQr = qrValue;
        isQrVisible.setValue(true);
    }

    public void onQrNotVisible() {
        isQrVisible.setValue(false);
    }

    public void logout() {
        logoutUseCase.execute();
        logoutSuccess.setValue(true);
    }
}


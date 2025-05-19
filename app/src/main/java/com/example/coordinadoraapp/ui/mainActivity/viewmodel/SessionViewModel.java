package com.example.coordinadoraapp.ui.mainActivity.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coordinadoraapp.domain.usecase.LogoutUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SessionViewModel extends ViewModel {

    private final LogoutUseCase logoutUseCase;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Boolean> _logoutSuccess = new MutableLiveData<>();
    public final LiveData<Boolean> logoutSuccess = _logoutSuccess;

    @Inject
    public SessionViewModel(LogoutUseCase logoutUseCase) {
        this.logoutUseCase = logoutUseCase;
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

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}

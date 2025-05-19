package com.example.coordinadoraapp.ui.mainActivity.viewmodel;

import android.util.Base64;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coordinadoraapp.domain.usecase.ValidateRawInputUseCase;
import com.example.coordinadoraapp.ui.mainActivity.state.RawInputUiState;
import com.example.coordinadoraapp.ui.mapper.LocationUiMapper;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RawInputViewModel extends ViewModel {

    private final ValidateRawInputUseCase validateRawInputUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<RawInputUiState> _rawInputUiState = new MutableLiveData<>();
    public final LiveData<RawInputUiState> rawInputUiState = _rawInputUiState;

    @Inject
    public RawInputViewModel(ValidateRawInputUseCase validateRawInputUseCase) {
        this.validateRawInputUseCase = validateRawInputUseCase;
    }

    public void submit(String rawText) {
        String base64 = Base64.encodeToString(rawText.getBytes(), Base64.NO_WRAP);
        _rawInputUiState.setValue(new RawInputUiState.Loading());
        disposables.add(validateRawInputUseCase.execute(base64)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                validatedData -> _rawInputUiState.setValue(new RawInputUiState.Success(LocationUiMapper.toUi(validatedData))),
                throwable -> _rawInputUiState.setValue(new RawInputUiState.Error(throwable.getMessage()))
            ));
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}

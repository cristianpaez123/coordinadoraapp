package com.example.coordinadoraapp.di.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.coordinadoraapp.domain.usecase.LogoutUseCase;
import com.example.coordinadoraapp.ui.Map.MapViewModel;
import com.example.coordinadoraapp.ui.login.LoginViewModel;
import com.example.coordinadoraapp.ui.mainActivity.MainActivityViewModel;
import com.example.coordinadoraapp.ui.mainActivity.viewmodel.QrScannerViewModel;
import com.example.coordinadoraapp.ui.mainActivity.viewmodel.RawInputViewModel;
import com.example.coordinadoraapp.ui.mainActivity.viewmodel.SessionViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    abstract ViewModel bindMainActivityViewModel(MainActivityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(QrScannerViewModel.class)
    abstract ViewModel bindQrScannerViewModel(QrScannerViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RawInputViewModel.class)
    abstract ViewModel bindRawInputViewModel(RawInputViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SessionViewModel.class)
    abstract ViewModel bindSessionViewModel(SessionViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel.class)
    abstract ViewModel bindMapViewModel(MapViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}


package com.example.coordinadoraapp.di.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.coordinadoraapp.ui.Map.MapViewModel;
import com.example.coordinadoraapp.ui.login.LoginViewModel;
import com.example.coordinadoraapp.ui.mainActivity.viewmodel.QrScannerViewModel;
import com.example.coordinadoraapp.ui.mainActivity.viewmodel.LocationViewModel;
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
    @ViewModelKey(QrScannerViewModel.class)
    abstract ViewModel bindQrScannerViewModel(QrScannerViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LocationViewModel.class)
    abstract ViewModel bindRawInputViewModel(LocationViewModel viewModel);

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


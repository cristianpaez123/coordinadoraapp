package com.example.coordinadoraapp.di;

import android.content.Context;

import com.example.coordinadoraapp.data.local.LocationLocalDataSource;
import com.example.coordinadoraapp.data.repository.MainActivityImpl;
import com.example.coordinadoraapp.domain.mainActivity.MainActivityRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Context context;


    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    FirebaseFirestore provideFirebaseFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    @Singleton
    BarcodeScanner provideBarcodeScanner() {
        BarcodeScannerOptions options =
            new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();
        return BarcodeScanning.getClient(options);
    }

    @Provides
    MainActivityRepository provideMainActivityRepository(BarcodeScanner scanner) {
        return new MainActivityImpl(scanner);
    }

    @Provides
    @Singleton
    public LocationLocalDataSource provideLocalDataSource(Context context) {
        return new LocationLocalDataSource(context);
    }
}

package com.example.coordinadoraapp.domain.mainActivity;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import java.util.List;
import io.reactivex.rxjava3.core.Single;

public interface MainActivityRepository {
    Single<List<Barcode>> processImage(InputImage image);

}

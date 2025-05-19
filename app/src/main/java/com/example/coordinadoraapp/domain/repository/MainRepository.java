package com.example.coordinadoraapp.domain.repository;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import java.util.List;
import io.reactivex.rxjava3.core.Single;

public interface MainRepository {
    Single<List<Barcode>> processImage(InputImage image);

}

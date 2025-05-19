package com.example.coordinadoraapp.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.coordinadoraapp.domain.model.Location;

import io.reactivex.rxjava3.core.Completable;

public class LocationLocalDataSource {

    private final LocationDbHelper dbHelper;

    public LocationLocalDataSource(Context context) {
        this.dbHelper = new LocationDbHelper(context);
    }

    public Completable saveLocation(Location location) {
        return Completable.fromAction(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("label", location.label);
            values.put("latitude", location.latitude);
            values.put("longitude", location.longitude);
            values.put("observation", location.observation);

            db.insert(LocationDbHelper.TABLE_NAME, null, values);
            db.close();
        });
    }
}

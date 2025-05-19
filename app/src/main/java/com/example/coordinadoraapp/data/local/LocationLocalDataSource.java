package com.example.coordinadoraapp.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coordinadoraapp.domain.model.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class LocationLocalDataSource {

    private final LocationDbHelper dbHelper;

    // Column names (you could also extract these as static final constants in LocationDbHelper)
    private static final String COLUMN_LABEL = "label";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_OBSERVATION = "observation";

    public LocationLocalDataSource(Context context) {
        this.dbHelper = new LocationDbHelper(context);
    }

    public Completable saveLocation(Location location) {
        return Completable.fromAction(() -> {
            try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
                ContentValues values = buildContentValues(location);
                db.insert(LocationDbHelper.TABLE_NAME, null, values);
            }
        });
    }

    public Completable saveAllLocations(List<Location> locations) {
        return Completable.fromAction(() -> {
            try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
                db.beginTransaction();
                try {
                    for (Location location : locations) {
                        ContentValues values = buildContentValues(location);
                        db.insert(LocationDbHelper.TABLE_NAME, null, values);
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
        });
    }

    public Single<List<Location>> getAll() {
        return Single.fromCallable(() -> {
            List<Location> locations = new ArrayList<>();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = null;

            try {
                cursor = db.query(
                    LocationDbHelper.TABLE_NAME,
                    new String[]{COLUMN_LABEL, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_OBSERVATION},
                    null, null, null, null,
                    COLUMN_LABEL + " ASC" // optional sort by label
                );

                while (cursor.moveToNext()) {
                    String label = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LABEL));
                    String latitude = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
                    String longitude = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));
                    String observation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OBSERVATION));

                    locations.add(new Location(label, latitude, longitude, observation));
                }
            } finally {
                if (cursor != null) cursor.close();
                db.close();
            }

            return locations;
        });
    }

    public Completable clearAll() {
        return Completable.fromAction(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(LocationDbHelper.TABLE_NAME, null, null);
            db.close();
        });
    }

    private ContentValues buildContentValues(Location location) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LABEL, location.label);
        values.put(COLUMN_LATITUDE, location.latitude);
        values.put(COLUMN_LONGITUDE, location.longitude);
        values.put(COLUMN_OBSERVATION, location.observation);
        return values;
    }

}

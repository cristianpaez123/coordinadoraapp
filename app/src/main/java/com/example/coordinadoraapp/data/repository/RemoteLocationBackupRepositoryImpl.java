package com.example.coordinadoraapp.data.repository;

import com.example.coordinadoraapp.domain.model.Location;
import com.example.coordinadoraapp.domain.repository.RemoteLocationBackupRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Singleton
public class RemoteLocationBackupRepositoryImpl implements RemoteLocationBackupRepository {

    private final FirebaseFirestore firestore;
    private final FirebaseAuth auth;

    private static final String COLLECTION_BACKUP = "backup";
    private static final String SUBCOLLECTION_LOCATIONS = "locations";
    private static final String ERROR_USER_NOT_AUTHENTICATED = "User is not authenticated.";
    private static final String ERROR_SAVE_FAILED = "Failed to save location to Firestore";

    @Inject
    public RemoteLocationBackupRepositoryImpl(FirebaseFirestore firestore, FirebaseAuth auth) {
        this.firestore = firestore;
        this.auth = auth;
    }

    @Override
    public Completable backupLocation(Location location) {
        return Completable.create(emitter -> {
            if (auth.getCurrentUser() == null) {
                emitter.onError(new IllegalStateException(ERROR_USER_NOT_AUTHENTICATED));
                return;
            }

            String userId = auth.getCurrentUser().getUid();

            firestore.collection(COLLECTION_BACKUP)
                .document(userId)
                .collection(SUBCOLLECTION_LOCATIONS)
                .add(location)
                .addOnSuccessListener(documentRef -> emitter.onComplete())
                .addOnFailureListener(error ->
                    emitter.onError(new Exception(ERROR_SAVE_FAILED, error))
                );
        });
    }

    @Override
    public Single<List<Location>> getBackedUpLocations() {
        return Single.create(emitter -> {
            String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
            firestore.collection(COLLECTION_BACKUP)
                .document(uid)
                .collection(SUBCOLLECTION_LOCATIONS)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Location> locations = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Location location = doc.toObject(Location.class);
                        locations.add(location);
                    }
                    emitter.onSuccess(locations);
                })
                .addOnFailureListener(emitter::onError);
        });
    }
}

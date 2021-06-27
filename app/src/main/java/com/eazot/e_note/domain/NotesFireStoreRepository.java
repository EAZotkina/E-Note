package com.eazot.e_note.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NotesFireStoreRepository implements NotesRepository {

    public static final NotesRepository INSTANCE = new NotesFireStoreRepository();
    private final static String NOTES = "notes";
    private final static String DATE = "date";
    private final static String TITLE = "title";
    private final static String IMAGE = "image";
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    public void getNotes(Callback<List<Note>> callback) {

        firebaseFirestore.collection(NOTES)
                .orderBy(DATE, Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        ArrayList<Note> result = new ArrayList<>();

                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String title = (String) document.get(TITLE);
                            String image = (String) document.get(IMAGE);
                            Date date = ((Timestamp) Objects.requireNonNull(document.get(DATE))).toDate();

                            result.add(new Note(document.getId(), title, image, date));
                        }

                        callback.onSuccess(result);

                    } else {
                        task.getException();
                    }
                });
    }


    @Override
    public void clear() {

    }

    @Override
    public void add(String title, String imageUrl, Callback<Note> callback) {
        HashMap<String, Object> data = new HashMap<>();

        Date date = new Date();

        data.put(TITLE, title);
        data.put(IMAGE, imageUrl);
        data.put(DATE, date);

        firebaseFirestore.collection(NOTES)
                .add(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Note note = new Note(Objects.requireNonNull(task.getResult()).getId(), title, imageUrl, date);

                        callback.onSuccess(note);
                    }
                });
    }


    @Override
    public void remove(Note note, Callback<Object> callback) {
        firebaseFirestore.collection(NOTES)
                .document(note.getId())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(note);
                    }
                });

    }

    @Override
    public Note update(@NonNull Note note, @Nullable String title, @Nullable Date date) {
        return null;
    }
}
package com.eazot.e_note.domain;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

    public interface NotesRepository {

        void getNotes(Callback<List<Note>> callback);

        void clear();

        Note add(String title, String imageUrl);

        void remove(Note note);

        Note update(@NonNull Note note, @Nullable String title, @Nullable Date date);
    }

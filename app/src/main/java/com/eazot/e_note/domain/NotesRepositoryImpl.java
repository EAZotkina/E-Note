package com.eazot.e_note.domain;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotesRepositoryImpl implements NotesRepository {

    public static final NotesRepository INSTANCE = new NotesRepositoryImpl();

    private final ArrayList<Note> notes = new ArrayList<>();

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final Handler handler = new Handler(Looper.getMainLooper());

    public NotesRepositoryImpl() {

            notes.add(new Note("id1", "Понедельник", "https://avatars.mds.yandex.net/get-zen_doc/138668/pub_5e956dd89c87a00d2d7d6b43_5e956fde8837852659c74b10/scale_1200", new Date()));
            notes.add(new Note("id2", "Вторник", "https://simple-fauna.ru/wp-content/uploads/2018/03/skolko-zhivut-enoty.jpg", new Date()));
            notes.add(new Note("id3", "Среда", "https://krut-art.ru/wp-content/uploads/2021/05/dobrogo-utra-sredy-i-vkusnogo-zavtraka.jpg", new Date()));
            notes.add(new Note("id4", "Четверг", "https://krut-art.ru/wp-content/uploads/2021/05/Bodrogo-chetverga.jpg", new Date()));
            notes.add(new Note("id5", "Пятница", "data:image/jpeg;base64,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", new Date()));
            notes.add(new Note("id6", "Суббота", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTpxRi3jHFgUzrHW53UJWO0xQdJYzuRsDmXeQ&usqp=CAU", new Date()));
            notes.add(new Note("id7", "Воскресенье", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTvy3tYkvJyQJORJtuaf9yyNiHsL3VCoT794A&usqp=CAU", new Date()));

        }

        @Override
        public void getNotes(Callback<List<Note>> callback) {
            executor.execute(() -> {

                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(() -> callback.onSuccess(notes));
            });
        }

        @Override
        public void clear() {
            notes.clear();
        }

        @Override
        public Note add(String title, String imageUrl) {
            Note note = new Note(UUID.randomUUID().toString(), title, imageUrl, new Date());
            notes.add(note);
            return note;
        }

        @Override
        public void remove(Note note) {
            notes.remove(note);
        }

        @Override
        public Note update(@NonNull Note note, @Nullable String title, @Nullable Date date) {

            for (int i = 0; i < notes.size(); i++) {

                Note item = notes.get(i);

                if (item.getId().equals(note.getId())) {

                    String titleToSet = item.getTitle();
                    Date dateToSet = item.getDate();


                    if (title != null) {
                        titleToSet = title;
                    }

                    if (date != null) {
                        dateToSet = date;
                    }

                    Note newNote = new Note(note.getId(), titleToSet, note.getUrl(), dateToSet);

                    notes.remove(i);
                    notes.add(i, newNote);

                    return newNote;
                }
            }
            return note;
        }
    }


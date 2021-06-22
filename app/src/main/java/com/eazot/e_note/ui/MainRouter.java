package com.eazot.e_note.ui;

import androidx.fragment.app.FragmentManager;

import com.eazot.e_note.R;
import com.eazot.e_note.domain.Note;
import com.eazot.e_note.ui.info.InfoFragment;
import com.eazot.e_note.ui.notes.NotesFragment;
import com.eazot.e_note.ui.update.UpdateNoteFragment;

public class MainRouter {

    private final FragmentManager fragmentManager;

    public MainRouter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void showNotes() {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, NotesFragment.newInstance(), NotesFragment.TAG)
                .commit();
    }

    public void showInfo() {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, InfoFragment.newInstance(), InfoFragment.TAG)
                .commit();
    }

    public void showNoteDetail(Note note) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, NoteDetailsFragment.newInstance(note), NoteDetailsFragment.TAG)
                .addToBackStack(NoteDetailsFragment.TAG)
                .commit();
    }


    public void showEditNote(Note note) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, UpdateNoteFragment.newInstance(note), UpdateNoteFragment.TAG)
                .addToBackStack(UpdateNoteFragment.TAG)
                .commit();
    }

    public void back() {
        fragmentManager.popBackStack();
    }

}

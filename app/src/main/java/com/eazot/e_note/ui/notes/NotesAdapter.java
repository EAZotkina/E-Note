package com.eazot.e_note.ui.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eazot.e_note.R;
import com.eazot.e_note.domain.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private final Fragment fragment;
    private final ArrayList<Note> notes = new ArrayList<>();
    private OnNoteClickedListener listener;
    private OnNoteLongClickedListener longClickedListener;

    public NotesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public OnNoteLongClickedListener getLongClickedListener() {
        return longClickedListener;
    }

    public void setLongClickedListener(OnNoteLongClickedListener longClickedListener) {
        this.longClickedListener = longClickedListener;
    }

    public void setData(List<Note> toSet) {
        notes.clear();
        notes.addAll(toSet);
    }

    public int add(Note addedNote) {
        notes.add(addedNote);
        return notes.size() - 1;
    }

    public void remove(Note longClickedNote) {
        notes.remove(longClickedNote);
    }

    public void update(Note note) {
        for (int i = 0; i < notes.size(); i++) {

            Note item = notes.get(i);

            if (item.getId().equals(note.getId())) {

                notes.remove(i);
                notes.add(i, note);

                return;
            }
        }
    }

    public OnNoteClickedListener getListener() {
        return listener;
    }

    public void setListener(OnNoteClickedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public interface OnNoteClickedListener {
        void onNoteClickedListener(@NonNull Note note);
    }

    public interface OnNoteLongClickedListener {
        void onNoteLongClickedListener(@NonNull Note note, int index);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final ImageView image;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            fragment.registerForContextMenu(itemView);

            itemView.setOnClickListener(v -> {
                if (getListener() != null) {
                    getListener().onNoteClickedListener(notes.get(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(v -> {
                itemView.showContextMenu();

                if (getLongClickedListener() != null) {
                    int index = getAdapterPosition();
                    getLongClickedListener().onNoteLongClickedListener(notes.get(index), index);
                }

                return true;
            });

            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
        }

        public void bind(Note note) {
            title.setText(note.getTitle());

            Glide.with(image)
                    .load(note.getUrl())
                    .centerCrop()
                    .into(image);
        }
    }
}
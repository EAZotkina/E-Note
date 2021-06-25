package com.eazot.e_note.ui.notes;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eazot.e_note.R;
import com.eazot.e_note.RouterHolder;
import com.eazot.e_note.domain.Note;
import com.eazot.e_note.domain.NotesRepository;
import com.eazot.e_note.domain.NotesRepositoryImpl;
import com.eazot.e_note.ui.MainRouter;
import com.eazot.e_note.ui.update.UpdateNoteFragment;

import java.util.Collections;

public class NotesFragment extends Fragment {

    public static final String TAG = "NotesFragment";
    private final NotesRepository repository = NotesRepositoryImpl.INSTANCE;
    private NotesAdapter notesAdapter;

    private int longClickedIndex;
    private Note longClickedNote;

    private boolean isLoading = false;

    private ProgressBar progressBar;

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notesAdapter = new NotesAdapter(this);

        isLoading = true;

        repository.getNotes(result -> {
            notesAdapter.setData(result);
            notesAdapter.notifyDataSetChanged();

            isLoading = false;

            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        });

        notesAdapter.setListener(note -> {

            if (requireActivity() instanceof RouterHolder) {
                MainRouter router = ((RouterHolder) requireActivity()).getMainRouter();

                router.showNoteDetail(note);
            }
        });

        notesAdapter.setLongClickedListener((note, index) -> {
            longClickedIndex = index;
            longClickedNote = note;
        });

        getParentFragmentManager().setFragmentResultListener(UpdateNoteFragment.UPDATE_RESULT, this, (requestKey, result) -> {
            if (result.containsKey(UpdateNoteFragment.ARG_NOTE)) {
                Note note = result.getParcelable(UpdateNoteFragment.ARG_NOTE);

                notesAdapter.update(note);

                notesAdapter.notifyItemChanged(longClickedIndex);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        RecyclerView notesList = view.findViewById(R.id.notes_list);

        DefaultItemAnimator animator = new DefaultItemAnimator();

        animator.setAddDuration(5000L);
        animator.setRemoveDuration(7000L);

        notesList.setItemAnimator(animator);

        progressBar = view.findViewById(R.id.progress);

        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        }

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_add) {

                Note addedNote = repository.add("Новый день енота", "https://givotniymir.ru/wp-content/uploads/2016/05/enot-poloskun-obraz-zhizni-i-sreda-obitaniya-enota-poloskuna-3.jpg");

                int index = notesAdapter.add(addedNote);

                notesAdapter.notifyItemInserted(index);

                notesList.scrollToPosition(index);

                return true;
            }

            if (item.getItemId() == R.id.action_clear) {

                repository.clear();

                notesAdapter.setData(Collections.emptyList());

                notesAdapter.notifyDataSetChanged();

                return true;
            }

            return false;
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());

        notesList.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_separator));

        notesList.addItemDecoration(dividerItemDecoration);

        notesList.setAdapter(notesAdapter);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        requireActivity().getMenuInflater().inflate(R.menu.menu_notes_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_update) {

            if (requireActivity() instanceof RouterHolder) {
                MainRouter router = ((RouterHolder) requireActivity()).getMainRouter();

                router.showEditNote(longClickedNote);
            }

            return true;
        }

        if (item.getItemId() == R.id.action_delete) {

            repository.remove(longClickedNote);

            notesAdapter.remove(longClickedNote);

            notesAdapter.notifyItemRemoved(longClickedIndex);

            return true;
        }

        return super.onContextItemSelected(item);
    }
}
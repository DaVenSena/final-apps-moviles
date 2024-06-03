package com.mobile.apps.proyectofinalappsmoviles.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.apps.proyectofinalappsmoviles.Adapters.NoteAdapter;
import com.mobile.apps.proyectofinalappsmoviles.Adapters.NoteAdapter;
import com.mobile.apps.proyectofinalappsmoviles.AddNoteActivity;
import com.mobile.apps.proyectofinalappsmoviles.Classes.Note;
import com.mobile.apps.proyectofinalappsmoviles.Classes.Note;
import com.mobile.apps.proyectofinalappsmoviles.R;

import java.util.ArrayList;

public class NotesFragment extends Fragment {
    private static final String ARG_MY_OBJECT = "notes";
    RecyclerView rcv_notes;
    ImageView img_addNote;
    ArrayList<Note> notes;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public static NotesFragment newInstance(ArrayList<Note> notes) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_MY_OBJECT, notes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        rcv_notes = view.findViewById(R.id.rcv_notes);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Note item = (Note) data.getSerializableExtra("result");
                                NoteAdapter adapter = (NoteAdapter) rcv_notes.getAdapter();
                                adapter.addNote(item);
                            }
                        }
                    }
                }
        );
        img_addNote = view.findViewById(R.id.btn_addNote);
        img_addNote.setOnClickListener(this::goToAddNoteActivity);
        if (getArguments() != null) {
            notes = getArguments().getParcelableArrayList(ARG_MY_OBJECT);
            rcv_notes.setLayoutManager(new LinearLayoutManager(getContext()));
            rcv_notes.setAdapter(new NoteAdapter(notes));
        }
        return view;
    }

    public void goToAddNoteActivity(View view) {
        Intent intent = new Intent(view.getContext(), AddNoteActivity.class);
        activityResultLauncher.launch(intent);
    }
}

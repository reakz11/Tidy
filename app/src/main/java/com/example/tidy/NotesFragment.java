package com.example.tidy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.tidy.adapters.NotesAdapter;
import com.example.tidy.detailActivities.NoteDetails;
import com.example.tidy.objects.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment implements NotesAdapter.NoteClickListener {


    public NotesFragment() {}

    List<Note> list;
    NotesAdapter adapter;


    public static Fragment getInstance() {
        return new NotesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_notes, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_notes);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        list.add(new Note("Test Title 1", "Test Content 1"));
        list.add(new Note("Test Title 2", "Test Content 2"));
        list.add(new Note("Test Title 3", "Test Content 3"));
        list.add(new Note("Test Title 4", "Test Content 4"));
        list.add(new Note("Test Title 5", "Test Content 5"));
        list.add(new Note("Test Title 6", "Test Content 6"));
        list.add(new Note("Test Title 7", "Test Content 7"));
        list.add(new Note("Test Title 8", "Test Content 8"));
        list.add(new Note("Test Title 9", "Test Content 9"));
        list.add(new Note("Test Title 10", "Test Content 10"));
        list.add(new Note("Test Title 11", "Test Content 11"));
        list.add(new Note("Test Title 12", "Test Content 12"));
        list.add(new Note("Test Title 13", "Test Content 13"));

        adapter = new NotesAdapter(getContext(), list, this);
        recyclerView.setAdapter(adapter);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onNoteClick(String noteTitle, String noteContent) {
        Intent intent = new Intent(getContext(), NoteDetails.class);
        intent.putExtra("note_title", noteTitle);
        intent.putExtra("note_content", noteContent);
        Log.v("NOTE_INTENT", "sending data to note: " + intent.getExtras());
        startActivity(intent);
    }
}

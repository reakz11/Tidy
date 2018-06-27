package com.example.tidy;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.tidy.detailActivities.NoteDetails;
import com.example.tidy.objects.Note;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.tidy.Utils.getUserId;

public class NotesFragment extends Fragment {


    public NotesFragment() {
    }

    @BindView(R.id.rv_notes)
    RecyclerView recyclerView;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    private Query query;
    private FirebaseRecyclerAdapter<Note, NoteHolder> mAdapter;
    private LinearLayoutManager llm;
    private DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();

    public static Fragment getInstance() {
        return new NotesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        query = mFirebaseDatabase
                .child("users")
                .child(getUserId())
                .child("notes");

        FirebaseRecyclerOptions<Note> options =
                new FirebaseRecyclerOptions.Builder<Note>()
                        .setQuery(query, Note.class)
                        .build();


        mAdapter = new FirebaseRecyclerAdapter<Note, NoteHolder>(options) {
            @Override
            final public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.note_item, parent, false);

                return new NoteHolder(view);
            }

            @Override
            public void onBindViewHolder(NoteHolder holder, final int position, final Note note) {
                final NoteHolder viewHolder = (NoteHolder) holder;
                viewHolder.noteTitleTv.setText(note.getTitle());
                viewHolder.noteContentTv.setText(note.getContent());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), NoteDetails.class);
                        intent.putExtra("title", note.getTitle());
                        intent.putExtra("content", note.getContent());
                        startActivity(intent);
                    }
                });

                viewHolder.deleteNoteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAdapter.getRef(viewHolder.getAdapterPosition()).removeValue();
                    }
                });
            }
        };

        mAdapter.notifyDataSetChanged();

        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadingIndicator.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mAdapter);
        mAdapter.startListening();

        if (savedInstanceState != null) {

            final int positionIndex = savedInstanceState.getInt("rv_position");
            final int topView = savedInstanceState.getInt("rv_top_view");
            Log.v("NotesFragment", "bundle contents: " + savedInstanceState);

            mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);

                    try {
                        llm.scrollToPositionWithOffset(positionIndex, topView);

                        Log.v("NotesFragment", "positionIndex is: " + positionIndex);
                        Log.v("NotesFragment", "topView is: " + topView);
                    } catch (NullPointerException e) {
                        Log.v("NotesFragment", "NullPointerException");
                    }
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int positionIndex;
        int topView;

        positionIndex = llm.findFirstVisibleItemPosition();
        View startView = recyclerView.getChildAt(0);

        topView = (startView == null) ? 0 : (startView.getTop() - recyclerView.getPaddingTop());

        outState.putInt("rv_position", positionIndex);
        Log.v("NotesFragment", "rv_position is: " + positionIndex);
        outState.putInt("rv_top_view", topView);
        Log.v("NotesFragment", "rv_top_view is: " + topView);
    }

    public static class NoteHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.note_title) TextView noteTitleTv;
        @BindView(R.id.note_content) TextView noteContentTv;
        @BindView(R.id.delete_btn) Button deleteNoteButton;

        private NoteHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

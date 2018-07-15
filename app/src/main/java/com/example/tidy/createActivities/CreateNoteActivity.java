package com.example.tidy.createActivities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tidy.R;
import com.example.tidy.objects.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.tidy.Utils.isEmpty;

public class CreateNoteActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab_save_note) FloatingActionButton fabSaveNote;
    @BindView(R.id.et_note_title) EditText noteTitleEditText;
    @BindView(R.id.et_note_content) EditText noteContentEditText;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fabSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(noteTitleEditText) || isEmpty(noteContentEditText)) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            R.string.no_note_data, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    saveNote();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            R.string.note_created, Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.create_new_note);
    }

    // get the note data to save in our firebase db
    void saveNote() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            id = user.getUid();
        } else {
            Log.v("Auth", "User ID is null");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("taskList").push().getKey();

        final Note note = new Note();
        note.setTitle(noteTitleEditText.getText().toString());
        note.setContent(noteContentEditText.getText().toString());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put( key, note.toFirebaseObject());
        database.getReference("users").child(id).child("notes").updateChildren(childUpdates);
    }
}

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
import com.example.tidy.objects.Project;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.tidy.Utils.getCurrentDateAndTime;
import static com.example.tidy.Utils.getDatabase;
import static com.example.tidy.Utils.isEmpty;

public class CreateProjectActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab_save_project) FloatingActionButton fabSaveProject;
    @BindView(R.id.et_project_title) EditText projectTitleEditText;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_project);

        getDatabase();

        ButterKnife.bind(this);

        // If clicked and project title is not empty, save project
        fabSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmpty(projectTitleEditText)){
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.no_project_title, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    saveProject();
                    finish();
                }
            }
        });

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.create_new_project);
    }

    // get the project data to save in our firebase db
    void saveProject() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            id = user.getUid();
        } else {
            Log.v("Auth", "User ID is null");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("taskList").push().getKey();
        String projectId = getCurrentDateAndTime();

        final Project project = new Project();
        project.setTitle(projectTitleEditText.getText().toString());
        project.setId(projectId);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put( key, project.toFirebaseObject());
        database.getReference("users").child(id).child("projects").updateChildren(childUpdates);
    }
}

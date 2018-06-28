package com.example.tidy.detailActivities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.tidy.ProjectCategory;
import com.example.tidy.R;
import com.example.tidy.createActivities.CreateNoteActivity;
import com.example.tidy.createActivities.CreateProjectActivity;
import com.example.tidy.createActivities.CreateTaskActivity;
import com.example.tidy.objects.Project;
import com.example.tidy.objects.Task;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

import static com.example.tidy.Utils.getDatabase;
import static com.example.tidy.Utils.getUserId;

public class TaskDetails extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab_main) FloatingActionButton fabMain;
    @BindView(R.id.fab_project) FloatingActionButton fabProject;
    @BindView(R.id.fab_note) FloatingActionButton fabNote;
    @BindView(R.id.fab_task) FloatingActionButton fabTask;
    @BindView(R.id.layout_fab_project) LinearLayout layoutFabProject;
    @BindView(R.id.layout_fab_note) LinearLayout layoutFabNote;
    @BindView(R.id.layout_fab_task) LinearLayout layoutFabTask;
    @BindView(R.id.task_title_tv) TextView taskTitleTextView;
    @BindView(R.id.task_content_tv) TextView taskContentTextView;
    @BindView(R.id.task_date) TextView taskDateTextView;

    private Animation rotate_forward,rotate_backward;

    boolean isFABOpen=false;

    private String mTaskTitle = "title";
    private String mTaskContent = "content";
    private String mTaskDate = "date";
    private String mTaskId = "taskId";
    private String mTaskKey = "taskKey";

    private DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance()
            .getReference();

    Section<ProjectCategory,String> section;
    ExpandableLayout layout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_details);

        getDatabase();

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Task Details");

        // Getting data from intent list and displaying them
        final Intent intent = getIntent();

        if (intent.hasExtra(mTaskTitle)) {
            String taskTitle = intent.getStringExtra(mTaskTitle);

            taskTitleTextView.setText(taskTitle);
        }

        if (intent.hasExtra(mTaskContent)) {
            String taskContent = intent.getStringExtra(mTaskContent);

            taskContentTextView.setText(taskContent);
        }

        if (intent.hasExtra(mTaskDate)) {
            String taskDate = intent.getStringExtra(mTaskDate);

            taskDateTextView.setText(taskDate);
        }

        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        // Sets up click listener on main FAB and attaches opening/closing animations
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                    fabMain.startAnimation(rotate_forward);
                }else{
                    closeFABMenu();
                    fabMain.startAnimation(rotate_backward);
                }
            }
        });

        // Sets up click listeners on "menu" FABs
        // Click opens new activity and closes FAB menu
        fabTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMain.startAnimation(rotate_backward);
                closeFABMenu();
                startActivity(new Intent(getApplicationContext(), CreateTaskActivity.class));
            }
        });

        fabNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMain.startAnimation(rotate_backward);
                closeFABMenu();
                startActivity(new Intent(getApplicationContext(), CreateNoteActivity.class));
            }
        });

        fabProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMain.startAnimation(rotate_backward);
                closeFABMenu();
                startActivity(new Intent(getApplicationContext(), CreateProjectActivity.class));
            }
        });

        DatabaseReference projectsRef = mFirebaseDatabase
                .child("users")
                .child(getUserId())
                .child("projects");

        projectsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    layout = (ExpandableLayout) findViewById(R.id.el);

                    layout.setRenderer(new ExpandableLayout.Renderer<ProjectCategory,Project>(){

                        @Override
                        public void renderParent(View view, ProjectCategory projectCategory, boolean isExpanded, int parentPosition) {
                            ((TextView)view.findViewById(R.id.tv_parent_name)).setText(projectCategory.name);
                            view.findViewById(R.id.arrow).setBackgroundResource(isExpanded?
                                    R.mipmap.baseline_expand_less_black_24dp:R.mipmap.baseline_expand_more_black_24dp);
                        }

                        @Override
                        public void renderChild(View view, final Project project, int parentPosition, int childPosition) {
                            ((TextView)view.findViewById(R.id.tv_child_name)).setText(project.getTitle());
                            ((RadioButton)view.findViewById(R.id.radio_button)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    RadioButton radioButton = ((RadioButton)view.findViewById(R.id.radio_button));

                                    String projectId = String.valueOf(project.getId());
                                    String taskKey = intent.getStringExtra(mTaskKey);

                                    if (radioButton.isChecked()) {
                                        mFirebaseDatabase.child("users").child(getUserId())
                                                .child("tasks").child(taskKey).child("projectId").setValue(projectId);
                                    }

                                    Log.v("TaskDetails", "RenderChild onClick projectId is: " + projectId);

                                }
                            });
                        }
                    });

                    mFirebaseDatabase.child("users").child(getUserId())
                            .child("projects").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Map<String, String> td = (HashMap<String,String>) dataSnapshot.getValue();

                                List<String> values = new ArrayList<>(td.values());
                                JSONArray jsonArray = new JSONArray(values);
                                String jsonArrayStr = jsonArray.toString();

                                List<Project> projects = new ArrayList<Project>();
                                Type listType = new TypeToken<List<Project>>(){}.getType();
                                projects = new Gson().fromJson(jsonArrayStr, listType);

                                layout.addSection(getSection(projects));
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Log.v("TaskDetails", "No projects exist in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Section<ProjectCategory,Project> getSection(List<Project> values) {
        Section<ProjectCategory, Project> section = new Section<>();
        ProjectCategory projectCategory = new ProjectCategory("Projects");

        values.add(new Project("None","0"));

        section.parent = projectCategory;
        section.children.addAll(values);
        return section;
    }


    // Sets isFABOpen to TRUE, views to visible and creates opening animation
    private void showFABMenu(){
        isFABOpen=true;

        layoutFabProject.setVisibility(View.VISIBLE);
        layoutFabNote.setVisibility(View.VISIBLE);
        layoutFabTask.setVisibility(View.VISIBLE);

        layoutFabProject.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        layoutFabNote.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        layoutFabTask.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }

    // Sets isFABOpen to FALSE and creates closing animations
    // After last animation is finished, sets visibility of views to GONE
    private void closeFABMenu(){
        isFABOpen=false;

        layoutFabProject.animate().translationY(0);
        layoutFabNote.animate().translationY(0);
        layoutFabTask.animate().translationY(0).setListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    layoutFabProject.setVisibility(View.GONE);
                    layoutFabNote.setVisibility(View.GONE);
                    layoutFabTask.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

}

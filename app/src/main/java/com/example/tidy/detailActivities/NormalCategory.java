package com.example.tidy.detailActivities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tidy.R;
import com.example.tidy.createActivities.CreateNoteActivity;
import com.example.tidy.createActivities.CreateProjectActivity;
import com.example.tidy.createActivities.CreateTaskActivity;
import com.example.tidy.objects.Task;
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

import static com.example.tidy.Utils.getDatabase;
import static com.example.tidy.Utils.getOtherTimeValue;
import static com.example.tidy.Utils.getTomorrowDate;
import static com.example.tidy.Utils.getUserId;
import static com.example.tidy.Utils.getCurrentDate;

public class NormalCategory extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab_main) FloatingActionButton fabMain;
    @BindView(R.id.fab_project) FloatingActionButton fabProject;
    @BindView(R.id.fab_note) FloatingActionButton fabNote;
    @BindView(R.id.fab_task) FloatingActionButton fabTask;
    @BindView(R.id.layout_fab_project) LinearLayout layoutFabProject;
    @BindView(R.id.layout_fab_note) LinearLayout layoutFabNote;
    @BindView(R.id.layout_fab_task) LinearLayout layoutFabTask;
    @BindView(R.id.rv_tasks) RecyclerView recyclerView;
    @BindView(R.id.loading_indicator) ProgressBar loadingIndicator;


    private Animation rotate_forward,rotate_backward;

    private FirebaseRecyclerAdapter<Task, NormalCategory.TaskHolder> mAdapter;

    boolean isFABOpen=false;
    Query query;

    String supportActionBarTitle;
    private DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_category);

        getDatabase();

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            supportActionBarTitle = intent.getStringExtra(Intent.EXTRA_TEXT);
            getSupportActionBar().setTitle(supportActionBarTitle);

            switch (supportActionBarTitle) {
                case "Today":
                    query = mFirebaseDatabase
                            .child("users")
                            .child(getUserId())
                            .child("tasks")
                            .orderByChild("date")
                            .startAt(String.valueOf(getCurrentDate()))
                            .endAt(String.valueOf(getCurrentDate()));

                    break;
                case "Tomorrow":
                    query = mFirebaseDatabase
                            .child("users")
                            .child(getUserId())
                            .child("tasks")
                            .orderByChild("date")
                            .equalTo(String.valueOf(getTomorrowDate()));
                    break;
                default:
                    query = mFirebaseDatabase
                            .child("users")
                            .child(getUserId())
                            .child("tasks")
                            .orderByChild("date");
                    break;
            }
        }

        FirebaseRecyclerOptions<Task> options =
                new FirebaseRecyclerOptions.Builder<Task>()
                        .setQuery(query, Task.class)
                        .build();
        
        mAdapter = new FirebaseRecyclerAdapter<Task, TaskHolder>(options) {
            @Override
            final public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.task_item, parent, false);

                return new TaskHolder(view);
            }

            @Override
            public void onBindViewHolder(TaskHolder holder, final int position,final Task task) {
                final TaskHolder viewHolder = (TaskHolder) holder;
                viewHolder.taskTitle.setText(task.getTitle());
                viewHolder.taskContent.setText(task.getContent());
                if (task.getDate() != null) {
                    viewHolder.taskDate.setText(task.getFormattedDate());
                }

                viewHolder.taskCheckbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       mAdapter.getRef(viewHolder.getAdapterPosition()).child("state").setValue("1");
                       mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), TaskDetails.class);
                        intent.putExtra("title", task.getTitle());
                        intent.putExtra("content", task.getContent());
                        intent.putExtra("date", task.getFormattedDate());
                        Log.v("TASK_INTENT", "sending data to task: " + intent.getExtras());
                        startActivity(intent);
                    }
                });

                viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAdapter.getRef(viewHolder.getAdapterPosition()).removeValue();
                    }
                });

                if (supportActionBarTitle.equals("Today") || supportActionBarTitle.equals("Tomorrow")) {
                    if (String.valueOf(task.getState()).equals("1")) {
                        viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                    }
                } else {
                    if (task.getDate() != null && Integer.parseInt(task.getDate()) < getOtherTimeValue() || Integer.parseInt(task.getState()) == 1) {
                        viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                    }
                }


            }
        };
        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    loadingIndicator.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

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
    }

    public static class TaskHolder extends RecyclerView.ViewHolder {

        TextView taskTitle;
        TextView taskContent;
        TextView taskDate;

        Button deleteButton;
        CheckBox taskCheckbox;

        public TaskHolder(View itemView) {
            super(itemView);

            taskTitle = (TextView) itemView.findViewById(R.id.task_name);
            taskContent = (TextView) itemView.findViewById(R.id.task_content);
            taskDate = (TextView) itemView.findViewById(R.id.task_due_date);

            deleteButton = (Button) itemView.findViewById(R.id.delete_btn);
            taskCheckbox = (CheckBox) itemView.findViewById(R.id.task_check_box);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
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

package com.example.tidy.detailActivities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.tidy.R;
import com.example.tidy.adapters.TasksAdapter;
import com.example.tidy.createActivities.CreateNoteActivity;
import com.example.tidy.createActivities.CreateProjectActivity;
import com.example.tidy.createActivities.CreateTaskActivity;
import com.example.tidy.objects.Task;

import java.util.ArrayList;
import java.util.List;

public class NormalCategory extends AppCompatActivity implements TasksAdapter.TaskClickListener {

    private Toolbar toolbar;

    List<Task> list;
    TasksAdapter adapter;

    FloatingActionButton fabMain, fabProject, fabNote, fabTask;
    LinearLayout layoutFabProject, layoutFabNote, layoutFabTask;

    private Animation rotate_forward,rotate_backward;

    boolean isFABOpen=false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_category);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            String supportActionBarTitle = intent.getStringExtra(Intent.EXTRA_TEXT);
            getSupportActionBar().setTitle(supportActionBarTitle);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        list = new ArrayList<>();
        list.add(new Task("Very important task","08-06-2018", 0));
        list.add(new Task("Another very important task","08-06-2018", 0));
        list.add(new Task("Less important task","08-06-2018", 0));

        adapter = new TasksAdapter(getApplicationContext(),list,this);
        recyclerView.setAdapter(adapter);

        fabMain = (FloatingActionButton) findViewById(R.id.fab_main);
        fabProject = (FloatingActionButton) findViewById(R.id.fab_project);
        fabNote = (FloatingActionButton) findViewById(R.id.fab_note);
        fabTask = (FloatingActionButton) findViewById(R.id.fab_task);
        layoutFabProject = (LinearLayout) findViewById(R.id.layout_fab_project);
        layoutFabNote = (LinearLayout) findViewById(R.id.layout_fab_note);
        layoutFabTask = (LinearLayout) findViewById(R.id.layout_fab_task);

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

    @Override
    public void onTaskClick(String taskTitle, String date, int status) {
        Intent intent = new Intent(getApplicationContext(), TaskDetails.class);
        intent.putExtra(Intent.EXTRA_TEXT, taskTitle);
        Log.v("TASK_INTENT", "sending data to task: " + intent.getExtras());
        startActivity(intent);
    }

}

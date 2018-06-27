package com.example.tidy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tidy.adapters.ProjectsAdapter;
import com.example.tidy.detailActivities.FinishedTasksCategory;
import com.example.tidy.detailActivities.NormalCategory;
import com.example.tidy.detailActivities.ProjectDetails;
import com.example.tidy.objects.Note;
import com.example.tidy.objects.Project;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.tidy.Utils.getDatabase;
import static com.example.tidy.Utils.getUserId;

public class TasksFragment extends Fragment {

    public TasksFragment() {}

    @BindView(R.id.rv_projects) RecyclerView recyclerView;
    @BindView(R.id.card_today) CardView todayCard;
    @BindView(R.id.card_tomorrow) CardView tomorrowCard;
    @BindView(R.id.card_other_time) CardView otherTimeCard;
    @BindView(R.id.card_finished_tasks) CardView finishedTasksCard;
    @BindView(R.id.loading_indicator) ProgressBar loadingIndicator;

    private Query query;
    private FirebaseRecyclerAdapter<Project, ProjectHolder> mAdapter;
    private DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();


    public static Fragment getInstance() {
        return new TasksFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_tasks, container, false);

        ButterKnife.bind(this,rootView);
        getDatabase();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        query = mFirebaseDatabase
                .child("users")
                .child(getUserId())
                .child("projects");

        FirebaseRecyclerOptions<Project> options =
                new FirebaseRecyclerOptions.Builder<Project>()
                        .setQuery(query, Project.class)
                        .build();

        mAdapter = new FirebaseRecyclerAdapter<Project, ProjectHolder>(options) {
            @Override
            final public ProjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.project_item, parent, false);

                return new ProjectHolder(view);
            }

            @Override
            public void onBindViewHolder(ProjectHolder holder, final int position, final Project project) {
                final ProjectHolder viewHolder = (ProjectHolder) holder;
                viewHolder.projectNameTv.setText(project.getTitle());
                viewHolder.projectNameTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ProjectDetails.class);
                        intent.putExtra("title", project.getTitle());
                        intent.putExtra("id", project.getId());
                        startActivity(intent);
                    }
                });
                viewHolder.deleteProjectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAdapter.getRef(viewHolder.getAdapterPosition()).removeValue();
                        mAdapter.notifyDataSetChanged();
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

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setFocusable(false);

        mAdapter.startListening();

        // Sets onClickListener on CardViews
        todayCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NormalCategory.class);
                intent.putExtra(Intent.EXTRA_TEXT, "Today");
                startActivity(intent);
            }
        });

        tomorrowCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NormalCategory.class);
                intent.putExtra(Intent.EXTRA_TEXT, "Tomorrow");
                startActivity(intent);
            }
        });

        otherTimeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NormalCategory.class);
                intent.putExtra(Intent.EXTRA_TEXT, "Other Time");
                startActivity(intent);
            }
        });

        finishedTasksCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FinishedTasksCategory.class);
                startActivity(intent);
            }
        });
    }

    public static class ProjectHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.project_name) TextView projectNameTv;
        @BindView(R.id.delete_btn) Button deleteProjectBtn;

        private ProjectHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

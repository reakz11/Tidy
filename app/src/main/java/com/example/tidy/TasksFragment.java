package com.example.tidy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tidy.detailActivities.CompletedTasksCategory;
import com.example.tidy.detailActivities.NormalCategory;
import com.example.tidy.detailActivities.ProjectDetails;
import com.example.tidy.objects.Project;
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
import static com.example.tidy.Utils.getUserId;

public class TasksFragment extends Fragment {

    public TasksFragment() {}

    @BindView(R.id.rv_projects) RecyclerView recyclerView;
    @BindView(R.id.card_today) CardView todayCard;
    @BindView(R.id.card_tomorrow) CardView tomorrowCard;
    @BindView(R.id.card_other_time) CardView otherTimeCard;
    @BindView(R.id.card_completed_tasks) CardView completedTasksCard;
    @BindView(R.id.loading_indicator) ProgressBar loadingIndicator;
    @BindView(R.id.hint_no_projects) TextView hintNoProjects;

    private Query query;
    private LinearLayoutManager llm;
    private FirebaseRecyclerAdapter<Project, ProjectHolder> mAdapter;
    private DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();

    public static Fragment getInstance() {
        return new TasksFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_tasks, container, false);

        // Gets FirebaseDatabase and sets offline persistence to true
        getDatabase();

        // Binding views
        ButterKnife.bind(this,rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Sets details of query
        query = mFirebaseDatabase
                .child("users")
                .child(getUserId())
                .child("projects");

        FirebaseRecyclerOptions<Project> options =
                new FirebaseRecyclerOptions.Builder<Project>()
                        .setQuery(query, Project.class)
                        .build();

        // Creating new FirebaseRecyclerAdapter for projects
        mAdapter = new FirebaseRecyclerAdapter<Project, ProjectHolder>(options) {
            @Override
            final public ProjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.project_item, parent, false);

                return new ProjectHolder(view);
            }

            @Override
            public void onBindViewHolder(ProjectHolder holder, final int position, final Project project) {
                final ProjectHolder viewHolder = holder;
                // Sets project title
                if (project.getTitle() != null) {
                    viewHolder.projectNameTv.setText(project.getTitle());
                }

                // onClickListener used for opening details of clicked project
                // First it gets data of clicked project, puts them inside the intent
                // and then starts ProjectDetails activity
                viewHolder.projectNameTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ProjectDetails.class);
                        intent.putExtra("title", project.getTitle());
                        intent.putExtra("id", project.getId());
                        startActivity(intent);
                    }
                });

                // onClickListener used for removing project from DB
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

        llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setFocusable(false);

        mAdapter.startListening();

        mFirebaseDatabase.child("users").child(getUserId()).child("projects")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        hintNoProjects.setVisibility((mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE));
                        Log.v("TaskFragment", "mAdapter itemCount: " + mAdapter.getItemCount());
                        loadingIndicator.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        if (savedInstanceState != null) {

            // Used for saving scroll position
            final int positionIndex = savedInstanceState.getInt("rv_position_tasks");
            final int topView = savedInstanceState.getInt("rv_top_view_tasks");

            mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);

                    try {
                        llm.scrollToPositionWithOffset(positionIndex, topView);
                    } catch (NullPointerException e) {
                        Log.v("NotesFragment", "NullPointerException");
                    }
                }
            });
        }

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

        completedTasksCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CompletedTasksCategory.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int positionIndexTasks;
        int topViewTask;

        positionIndexTasks = llm.findFirstVisibleItemPosition();
        View startView = recyclerView.getChildAt(0);

        topViewTask = (startView == null) ? 0 : (startView.getTop() - recyclerView.getPaddingTop());

        outState.putInt("rv_position_tasks", positionIndexTasks);
        outState.putInt("rv_top_view_tasks", topViewTask);
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

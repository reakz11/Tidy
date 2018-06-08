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

import com.example.tidy.adapters.ProjectsAdapter;
import com.example.tidy.createActivities.CreateProjectActivity;
import com.example.tidy.detailActivities.FinishedTasksCategory;
import com.example.tidy.detailActivities.NormalCategory;
import com.example.tidy.detailActivities.NoteDetails;
import com.example.tidy.detailActivities.ProjectDetails;
import com.example.tidy.objects.Project;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment implements ProjectsAdapter.ProjectClickListener {

    public TasksFragment() {}

    List<Project> list;

    ProjectsAdapter adapter;
    CardView todayCard, tomorrowCard, otherTimeCard, finishedTasksCard;

    public static Fragment getInstance() {
        return new TasksFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_tasks, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_projects);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        list.add(new Project("Project 1"));
        list.add(new Project("Project 2"));
        list.add(new Project("Project 3"));
        list.add(new Project("Project 4"));
        list.add(new Project("Project 5"));
        list.add(new Project("Project 6"));
        list.add(new Project("Project 7"));
        list.add(new Project("Project 8"));
        list.add(new Project("Project 9"));
        list.add(new Project("Project 10"));
        list.add(new Project("Project 11"));


        adapter = new ProjectsAdapter(getContext(), list, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        todayCard = (CardView) view.findViewById(R.id.card_today);
        tomorrowCard = (CardView) view.findViewById(R.id.card_tomorrow);
        otherTimeCard = (CardView) view.findViewById(R.id.card_other_time);
        finishedTasksCard = (CardView) view.findViewById(R.id.card_finished_tasks);

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

    @Override
    public void onProjectClick(String projectTitle) {
        Intent intent = new Intent(getContext(), ProjectDetails.class);
        intent.putExtra(Intent.EXTRA_TEXT, projectTitle);
        startActivity(intent);
    }

}

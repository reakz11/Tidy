package com.example.tidy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tidy.adapters.ProjectsAdapter;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {

    public TasksFragment() {}

    List<String> list;
    ProjectsAdapter adapter;


    public static Fragment getInstance() {
        return new TasksFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_tasks, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_projects);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        list.add("Test 1");
        list.add("Test 2");
        list.add("Test 3");
        list.add("Test 4");
        list.add("Test 5");
        list.add("Test 6");
        list.add("Test 7");
        list.add("Test 8");

        adapter = new ProjectsAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}

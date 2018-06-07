package com.example.tidy.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tidy.R;

import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectsViewHolder> {

    Context context;
    List<String> list;

    public ProjectsAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_item, parent, false);
        return new ProjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull ProjectsViewHolder holder,int position){
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount () {
        return list.size();
    }

    class ProjectsViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ProjectsViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.project_name);
        }
    }
}

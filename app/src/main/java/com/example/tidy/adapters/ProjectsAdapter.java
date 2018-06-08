package com.example.tidy.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tidy.R;
import com.example.tidy.objects.Project;

import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectsViewHolder> {

    Context context;
    List<Project> list;

    final private ProjectClickListener mOnClickListener;

    public interface ProjectClickListener {
        void onProjectClick (String projectTitle);
    }

    public ProjectsAdapter(Context context, List<Project> list, ProjectClickListener listener) {
        this.list = list;
        this.context = context;
        mOnClickListener = listener;
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
        Project object = list.get(position);
        holder.projectTitle.setText(object.getTitle());
    }

    @Override
    public int getItemCount () {
        return list.size();
    }

    class ProjectsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView projectTitle;

        public ProjectsViewHolder(View itemView) {
            super(itemView);
            projectTitle = (TextView) itemView.findViewById(R.id.project_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String projectTitle = list.get(adapterPosition).getTitle();
            mOnClickListener.onProjectClick(projectTitle);
        }
    }
}

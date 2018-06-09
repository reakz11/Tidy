package com.example.tidy.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.tidy.R;
import com.example.tidy.objects.Task;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    Context context;
    List<Task> list;

    final private TaskClickListener mOnClickListener;

    public interface TaskClickListener {
        void onTaskClick(String taskTitle, String date);
    }

    public TasksAdapter(Context context, List<Task> list, TaskClickListener listener) {
        this.list = list;
        this.context = context;
        mOnClickListener = listener;
    }


    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull TasksAdapter.TasksViewHolder holder,int position){
        Task object = list.get(position);
        holder.taskTitle.setText(object.getTitle());
        holder.dueDate.setText(object.getDate());
    }

    @Override
    public int getItemCount () {
        return list.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView taskTitle;
        TextView dueDate;
        TextView content;
        CheckBox checkBox;

        public TasksViewHolder(View itemView) {
            super(itemView);
            taskTitle = (TextView) itemView.findViewById(R.id.task_name);
            dueDate = (TextView) itemView.findViewById(R.id.task_due_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String taskTitle = list.get(adapterPosition).getTitle();

//            Date date = list.get(adapterPosition).getDate();
//            DateFormat dateFormat = DateFormat.getDateInstance();
//            String normalDate = dateFormat.format(date);
            String normalDate = list.get(adapterPosition).getDate();

            mOnClickListener.onTaskClick(taskTitle, normalDate);
        }
    }
}

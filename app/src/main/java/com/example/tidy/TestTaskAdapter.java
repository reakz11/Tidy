package com.example.tidy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tidy.objects.Task;

import java.util.ArrayList;

public class TestTaskAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Task> taskList;

    final private TestTaskClickListener mOnClickListener;

    public interface TestTaskClickListener {
        void onTaskClick(String taskTitle, String taskContent, String taskDueDate);
    }

    public TestTaskAdapter(Context context, ArrayList<Task> list, TestTaskClickListener listener) {
        this.taskList = list;
        this.context = context;
        mOnClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        SimpleItemViewHolder pvh = new SimpleItemViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SimpleItemViewHolder viewHolder = (SimpleItemViewHolder) holder;
        viewHolder.position = position;
        Task task = taskList.get(position);
        ((SimpleItemViewHolder) holder).title.setText(task.getTitle());
        ((SimpleItemViewHolder) holder).date.setText(task.getDate());
    }

    public final  class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView date;
        public int position;
        public SimpleItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.task_name);
            date = (TextView) itemView.findViewById(R.id.task_due_date);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String title = taskList.get(adapterPosition).getTitle();
            String content = taskList.get(adapterPosition).getContent();
            String date = taskList.get(adapterPosition).getDate();
            mOnClickListener.onTaskClick(title, content, date);
        }
    }
}

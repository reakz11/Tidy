package com.example.tidy.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tidy.Note;
import com.example.tidy.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    Context context;
    List<Note> list;

    public NotesAdapter(Context context, List<Note> list) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull NotesAdapter.NotesViewHolder holder, int position){
        Note object = list.get(position);
        holder.noteTitle.setText(object.getTitle());
        holder.noteContent.setText(object.getContent());
    }

    @Override
    public int getItemCount () {
        return list.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {

        TextView noteTitle;
        TextView noteContent;

        public NotesViewHolder(View itemView) {
            super(itemView);
            noteTitle = (TextView) itemView.findViewById(R.id.note_name);
            noteContent = (TextView) itemView.findViewById(R.id.note_content);
        }
    }

}

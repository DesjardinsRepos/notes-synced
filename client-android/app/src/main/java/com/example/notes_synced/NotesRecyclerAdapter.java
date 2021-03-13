package com.example.notes_synced;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {

    private List<Note> noteList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    NotesRecyclerAdapter(Context context, List<Note> noteList) {
        this.mInflater = LayoutInflater.from(context);
        this.noteList = noteList;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.note_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.noteTitle.setText(noteList.get(i).getTitle());
        holder.noteBody.setText(noteList.get(i).getBody());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return noteList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView noteTitle, noteBody;
        ImageButton trash;

        ViewHolder(View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteBody = itemView.findViewById(R.id.noteBody);
            trash = itemView.findViewById(R.id.trash);
            trash.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Note getItem(int id) {
        return noteList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
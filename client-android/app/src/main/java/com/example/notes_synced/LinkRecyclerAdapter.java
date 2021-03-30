package com.example.notes_synced;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// from https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
public class LinkRecyclerAdapter extends RecyclerView.Adapter<LinkRecyclerAdapter.ViewHolder> {

    private List<String> bodyText, bodyLink;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    LinkRecyclerAdapter(Context context, List<String> bodyText, List<String> bodyLink) {
        this.mInflater = LayoutInflater.from(context);
        this.bodyText = bodyText;
        this.bodyLink = bodyLink;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.edit_link, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        //if(!bodyLink.get(i).matches("^(?i)(https?|ftp)://.*$")) bodyLink.set(i, "https://www." + bodyLink.get(i));
        Log.d("BODY", bodyText.size() + bodyText.toString());
        Log.d("LINK", bodyLink.size() + bodyLink.toString());

        holder.editNoteBody.setText(bodyText.get(i));

        if(i != bodyText.size() - 1) holder.richLinkView.setLink(
            bodyLink.get(i).matches("^(?i)(https?|ftp)://.*$")
                ? bodyLink.get(i)
                : "https://" + bodyLink.get(i),
            new ponnamkarthik_ViewListener() {
                @Override
                public void onSuccess(boolean status) {
                    // remove preview constraints
                }

                @Override
                public void onError(Exception e) {

                }
            }
        );
    }

    @Override
    public int getItemCount() {
        return bodyText.size();
    }

    public int getTextAmount() { return bodyText.size(); }
    public int getLinkAmount() { return bodyLink.size(); }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView editNoteBody;
        ponnamkarthik_RichLinkView richLinkView;
        ConstraintLayout noteBackground;

        ViewHolder(View itemView) {
            super(itemView);
            editNoteBody = itemView.findViewById(R.id.editNoteBody);
            richLinkView = itemView.findViewById(R.id.richLinkView);
            noteBackground = itemView.findViewById(R.id.noteBackground);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
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
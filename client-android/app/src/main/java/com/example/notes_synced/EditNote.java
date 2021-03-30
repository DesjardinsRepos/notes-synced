package com.example.notes_synced;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Stopwatch;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EditNote extends Activity implements LinkRecyclerAdapter.ItemClickListener {
    private int i;
    EditText editNoteTitle, editNoteBody;
    RecyclerView recyclerView;
    LinkRecyclerAdapter adapter;
    List<String> bodyText = new ArrayList<>(), bodyLink = new ArrayList<>();
    private final boolean LINK_PREVIEW_ACTIVATED = false;

    @Override
    public void onItemClick(View view, int position) { // click events from NotesRecyclerAdapter

        if(view.getId() == R.id.noteBackground) { // open note
            startActivity(new Intent(this, EditNote.class).putExtra("index", position));

        } else if(view.getId() == R.id.trash) { // remove note
            initRecyclerView();
        }
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LinkRecyclerAdapter(this, bodyText, bodyLink);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) { // get note content from MainActivity.noteList
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        editNoteTitle = this.findViewById(R.id.editNoteTitle);
        editNoteBody = this.findViewById(R.id.editNoteBody);
        recyclerView = findViewById(R.id.linkRecyclerView);
        NestedScrollView scrollView = findViewById(R.id.scrollView);

        if(LINK_PREVIEW_ACTIVATED) {

            if(getIntent().getExtras() != null) {
                this.i = getIntent().getExtras().getInt("index");

                editNoteBody.setText(
                        MainActivity.noteList.get(i).getBody(),
                        TextView.BufferType.EDITABLE
                );
                editNoteBody.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);

                editNoteTitle.setText(
                        MainActivity.noteList.get(i).getTitle(),
                        TextView.BufferType.EDITABLE
                );
            }

        } else {
            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // set home bar color, from https://stackoverflow.com/questions/27839105/android-lollipop-change-navigation-bar-color
                getWindow().setNavigationBarColor(getResources().getColor(R.color.colorBackground));
            }

            Stopwatch stop = Stopwatch.createStarted();
            String body = "einasd stackoverflow.com https://notes-synced.web.app f";
            Matcher m = Patterns.WEB_URL.matcher(body);

            while(m.find()) {
                bodyLink.add(m.group());
                Log.d("url", bodyLink.get(0).toString());
            }

            for(int i = 0; i < bodyLink.size(); i++) {
                String[] s = body.split(bodyLink.get(i), 2);

                if(i > 0) {
                    bodyText.add(bodyLink.get(i - 1) + s[0]);
                } else {
                    bodyText.add(s[0]);
                }
                body = s[1];
            }

            if(bodyText.size() > 1) bodyText.add(bodyLink.get(bodyLink.size() - 1) + body); else bodyText.add(body);

            Log.d("BODY", bodyText.size() + bodyText.toString());
            Log.d("LINK", bodyLink.size() + bodyLink.toString());

            initRecyclerView();

            Log.d("time", Long.toString(stop.elapsed(TimeUnit.MICROSECONDS))); //795
        }
    }

    @Override
    public void onBackPressed() { // when pressing the back button, refresh the edited note

        if(LINK_PREVIEW_ACTIVATED) {
            //recyclerView.findViewHolderForAdapterPosition(0).

        } else {
            MainActivity.noteList.set(this.i, new Note(
                    editNoteTitle.getText().toString(),
                    editNoteBody.getText().toString()
            ));
        }

        super.onBackPressed();
    }
}

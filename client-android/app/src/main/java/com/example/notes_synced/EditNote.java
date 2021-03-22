package com.example.notes_synced;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EditNote extends Activity {
    private int i;
    EditText editNoteTitle, editNoteBody;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) { // get note content from MainActivity.noteList
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);

        editNoteTitle = this.findViewById(R.id.editNoteTitle);
        editNoteBody = this.findViewById(R.id.editNoteBody);

        if(getIntent().getExtras() != null) {
            this.i = getIntent().getExtras().getInt("index");

            editNoteBody.setText(
                MainActivity.noteList.get(i).getBody(),
                TextView.BufferType.EDITABLE
            );

            editNoteTitle.setText(
                MainActivity.noteList.get(i).getTitle(),
                TextView.BufferType.EDITABLE
            );
        }

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // set home bar color, from https://stackoverflow.com/questions/27839105/android-lollipop-change-navigation-bar-color
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorBackground));
        }

        /*
        Stopwatch stop = Stopwatch.createStarted();

        String body = "oasfd.dne two three aLink.com four five one two three aLink.com four five one two three aLink.com four five one two three aLink.com four five one two three aLink.com four five one two three aLink.com four five one two three aLink.com four five one two three aLink.com four five one two three aLink.com four five one two three aLink.com four five one two three aLink.com four five one two three aLink.com four five";

        Matcher m = Patterns.WEB_URL.matcher(body);
        List<String> urls = new ArrayList<>();
        List<String> textBetweenUrls = new ArrayList<>();

        while(m.find()) {
            urls.add(m.group());
            Log.d("url", urls.get(0).toString());
        }

        for(int i = 0; i < urls.size(); i++) {
            String[] s = body.split(urls.get(i), 2);
            textBetweenUrls.add(s[0]);
            body = s[1];
        }

        textBetweenUrls.add(body);

        Log.d("time", Long.toString(stop.elapsed(TimeUnit.MICROSECONDS))); //795
        */

        //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View v = inflater.inflate(R.layout.edit_note, null);

        ponnamkarthik_RichLinkView ponnamkarthikRichLinkView = (ponnamkarthik_RichLinkView) findViewById(R.id.richLinkView);
        ponnamkarthikRichLinkView.setLink("https://www.stackoverflow.com", new ponnamkarthik_ViewListener() {

            @Override
            public void onSuccess(boolean status) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void onBackPressed() { // when pressing the back button, refresh the edited note

        MainActivity.noteList.set(this.i, new Note(
            editNoteTitle.getText().toString(),
            editNoteBody.getText().toString()
        ));
        super.onBackPressed();
    }
}

package com.example.notes_synced;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

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

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // set home bar color, from https://stackoverflow.com/questions/27839105/android-lollipop-change-navigation-bar-color
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorBackground));
        }

        /*
        RichLinkView richLinkView = (RichLinkView) findViewById(R.id.richLinkView);
        //richLinkView.setAlpha(0f);
        richLinkView.setBackgroundColor( getResources().getColor(R.color.colorBackground));

        richLinkView.setLink("https://stackoverflow.com", new ViewListener() {

            @Override
            public void onSuccess(boolean status) {

            }

            @Override
            public void onError(Exception e) {

            }
        });*/
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

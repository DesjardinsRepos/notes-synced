package com.example.notes_synced;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class EditNote extends Activity {
    private int i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);


        EditText
                editNoteTitle = this.findViewById(R.id.editNoteTitle),
                editNoteBody = this.findViewById(R.id.editNoteBody);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
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
    }

    @Override
    public void onBackPressed() {
        Log.d("back", "pressed");


        EditText
                editNoteTitle = this.findViewById(R.id.editNoteTitle),
                editNoteBody = this.findViewById(R.id.editNoteBody);

        MainActivity.noteList.set(this.i, new Note(
            editNoteTitle.getText().toString(),
            editNoteBody.getText().toString()
        ));

        super.onBackPressed();
    }
}

package com.example.notes_synced;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TextView info = this.findViewById(R.id.info);

        if (getIntent().getExtras() != null) {
            info.setText("currently logged in with\n" + getIntent().getExtras().getString("email"));
        }
    }

    public void deleteAccount(View view) {
        // delete user
    }
}
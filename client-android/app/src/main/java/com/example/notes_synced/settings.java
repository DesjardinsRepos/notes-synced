package com.example.notes_synced;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
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

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // set home bar color, from https://stackoverflow.com/questions/27839105/android-lollipop-change-navigation-bar-color
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorBackground));
        }
    }

    public void deleteAccount(View view) {
        // delete user
    }
}
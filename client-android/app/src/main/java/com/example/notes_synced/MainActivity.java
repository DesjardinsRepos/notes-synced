package com.example.notes_synced;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, NotesRecyclerAdapter.ItemClickListener {

    private static final String TAG = "MainActivity";
    public static boolean status = false, initComplete = false;
    public static List<Note> noteList = new ArrayList<Note>();
    private static ProgressBar progress;
    RecyclerView recyclerView;
    NotesRecyclerAdapter adapter;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // initialize the program
        
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // set home bar color, from https://stackoverflow.com/questions/27839105/android-lollipop-change-navigation-bar-color
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorBackground));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progress = findViewById(R.id.progress);

        initRecyclerView();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog();
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) { // click events from NotesRecyclerAdapter

        if(view.getId() == R.id.noteBackground) { // open note
            startActivity(new Intent(this, EditNote.class).putExtra("index", position));

        } else if(view.getId() == R.id.trash) { // remove note
            noteList.remove(position);
            initRecyclerView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // option from (...) button clicked
        switch (item.getItemId()) {

            case R.id.action_logout: {
                AuthUI.getInstance().signOut(this);
                return true;
            }

            case R.id.action_settings: {
                startActivity(
                    new Intent(this, settings.class)
                        .putExtra("email", FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getEmail()
                        )
                );
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() { // initialize auth listener
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() { // when the application loses focus
        if(initComplete) update();
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) { // if no token found, start login
            startActivity(new Intent(this, loginRegister.class));
            finish();
            return;
        }

        firebaseAuth.getCurrentUser().getIdToken(true)
            .addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                @Override
                public void onSuccess(GetTokenResult getTokenResult) {
                    token = getTokenResult.getToken();
                    if(noteList.size() == 0) pullData(); else initRecyclerView(); // if no data available, check for updates
                    progress.setVisibility(View.GONE);
                }
            });
    }

    private void alertDialog() {  // add-note-dialog
        EditText edit = new EditText(this);
        edit.setTextColor(getColor(R.color.colorFontPrimary));

        new AlertDialog.Builder(this)
            .setTitle("Title")
            .setView(edit)
            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addNote(edit.getText().toString());
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void addNote(String title) {
        noteList.add(new Note(title, ""));
        initRecyclerView(); // reload recycler just in case
    }

    private boolean pullData() { // get data from backend and store it in noteList

        RequestQueue queue = new RequestQueue(
            new DiskBasedCache(getCacheDir(), 1024 * 1024),
            new BasicNetwork(new HurlStack())
        );
        queue.start();

        queue.add(new StringRequest(
            Request.Method.POST,
            "https://europe-west1-notes-synced.cloudfunctions.net/api/pullData",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray array = new JSONArray(
                            response
                                .substring(1, response.length() - 1)                            // remove outer "
                                .replace("\\\"", "\"")                       // replace \" with "
                                .replace("\\n", "\n")                        // make line breaks functioning
                                .replace("\\\\\\\\","\\\\")                  // other escape problems
                        );

                        for(int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            noteList.add(new Note(
                                object.getString("title"),
                                object.getString("body")
                            ));
                        }
                        initComplete = true;
                        initRecyclerView();
                        status = true;

                    } catch (JSONException e) {
                        e.printStackTrace();
                        status = false;
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(
                        MainActivity.this,
                        "there was an error when trying to pull your notes: " + error.toString(),
                        Toast.LENGTH_SHORT
                    )
                    .show();
                    status = false;
                }
            }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        });

        return status;
    }

    private boolean update() { // push data stored in noteList to backend
        progress.setVisibility(View.VISIBLE);

        RequestQueue queue = new RequestQueue(
                new DiskBasedCache(getCacheDir(), 1024 * 1024),
                new BasicNetwork(new HurlStack())
        );
        queue.start();

        try {
            JSONArray notes = new JSONArray();

            for(Note note : noteList) {
                notes.put(new JSONObject()
                    .put("title", note.getTitle())
                    .put("body", note.getBody())
                );
            }

            queue.add(new JsonObjectRequest(
                Request.Method.POST,
                "https://europe-west1-notes-synced.cloudfunctions.net/api/update",
                new JSONObject().put("notes", notes), // notes in { "notes": $notes } - form
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {
                        status = true;
                        progress.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                            MainActivity.this,
                            "there was an error when trying to update: " + error.toString(),
                            Toast.LENGTH_SHORT
                        )
                        .show();
                        status = false;
                        progress.setVisibility(View.GONE);
                    }
                }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + token);
                    return params;
                }
            });

            return status;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void initRecyclerView() { // initialize the notes list
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesRecyclerAdapter(this, noteList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void logNotes(String a) { // logging notes for debugging
        Log.d(TAG, "beginning note log with lenght " + noteList.size() + " at " + a);
        for(Note note : noteList) {
            Log.d("noteLog", "title: " + note.getTitle() + " body: " + note.getBody());
        }
    }
}
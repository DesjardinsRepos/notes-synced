package com.example.notes_synced;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, NotesRecyclerAdapter.ItemClickListener {

    private static final String TAG = "MainActivity";
    public static boolean status = false;
    public static List<Note> noteList = new ArrayList<Note>();
    RecyclerView recyclerView;
    NotesRecyclerAdapter adapter;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    public void onItemClick(View view, int position) {
        if(view.getId() == R.id.noteBackground) {
            // open note
            startActivity(new Intent(this, EditNote.class).putExtra("index", position));

        } else if(view.getId() == R.id.trash) {
            noteList.remove(position);
            initRecyclerView();
        }
    }

    private void alertDialog() {
        EditText edit = new EditText(this);

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
    }

    private Boolean logNotes(String a) {
        Log.d(TAG, "beginning note log with lenght " + noteList.size() + " at " + a);
        for(Note note : noteList) {
            Log.d("noteLog", "title: " + note.getTitle() + " body: " + note.getBody());
        }
        return true;
    }

    private boolean pullData() {

        RequestQueue queue = new RequestQueue(
            new DiskBasedCache(getCacheDir(), 1024 * 1024),
            new BasicNetwork(new HurlStack())
        );
        queue.start();

        try {
            StringRequest request = new StringRequest(
                Request.Method.POST,
                "https://europe-west1-notes-synced.cloudfunctions.net/api/pullData",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response.substring(1, response.length() - 1));

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
                            logNotes("atAdd");
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
                        Log.d("Error.Response", error.toString());
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
            };

            queue.add(request);
            return status;

        } catch(Exception e) {
            Log.d(TAG, e.toString());
            return false;
        }
    }

    private boolean update() {

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

            Log.d(TAG, notes.toString());
            JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "https://europe-west1-notes-synced.cloudfunctions.net/api/update",
                new JSONObject().put("notes", notes),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {
                        Log.d("Response", object.toString());
                        status = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(
                            MainActivity.this,
                            "there was an error when trying to update: " + error.toString(),
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
            };

            queue.add(request);
            return status;

        } catch(Exception e) {
            Log.d(TAG, e.toString());
            return false;
        }
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesRecyclerAdapter(this, noteList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }
      /*

    "notes": [
        {
            "title": "title",
            "body": "body"
        },
        {
            "title": "title",
            "body": "body"
        }
    ]

    */


    private void startLoginActivity() {
        startActivity(new Intent(this, loginRegister.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                AuthUI.getInstance().signOut(this);
                return true;

            case R.id.action_settings:
                startActivity(
                    new Intent(this, settings.class)
                        .putExtra("email", FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getEmail()
                        )
                );
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            startLoginActivity();
            return;
        }
        Log.d("test", "kekw");
        firebaseAuth.getCurrentUser().getIdToken(true)
            .addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                @Override
                public void onSuccess(GetTokenResult getTokenResult) {
                    token = getTokenResult.getToken();
                    if(noteList.size() == 0) pullData(); else initRecyclerView();
                }
            });
    }





    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // uname?

    public void createDocument(View view) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "handle");
        map.put("email", "email");
        map.put("image", "https://firebasestorage.googleapis.com/v0/b/notes-synced.appspot.com/o/defaultProfilePicture.png?alt=media");
        map.put("handle", "handle");
        map.put("notes", "notes");

        db.collection("users")
            //.document("handle")
            //.set(map) // set removes not specified props
            .add(map)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String id = documentReference.getId();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: ", e);
                }
            });
    }

    public void readDocument(View view) {
        db.collection("users").document("handle")
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d(TAG, "onSucess: " + documentSnapshot.getData());
                    Log.d(TAG, "onSucess: " + documentSnapshot.getId());
                    Log.d(TAG, "onSucess: " + documentSnapshot.getString("email"));

                    Note note = documentSnapshot.toObject(Note.class); // how do i get only the notes field?
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: ", e);
                }
            });
    }

    public void realtimeUpdate(View view) {
        db.collection("users").document("handle")
            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error != null) {
                        Log.e(TAG, "onEvent: ", error);
                        return;
                    }

                    if(value != null) {
                        Map<String, Object> data = value.getData();
                    } else {
                        Log.e(TAG, "onEvent: query snapshot was null");
                    }
                }
            });
    }
}


















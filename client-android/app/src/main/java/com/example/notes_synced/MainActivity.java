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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

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

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;
    private String token;

    List<Note> noteList = new ArrayList<Note>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               alertDialog();
            }
        });
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
        Note note = new Note(title, "");
        noteList.add(note);
        HashMap<String, String> h = new HashMap<>();
        h.put("email", "fabian3@fabian.fabian");
        h.put("password", "fabianfabian");
        Log.d(TAG, "token: " + token);

        //performPostCall("https://europe-west1-notes-synced.cloudfunctions.net/api/pullData", h);
        update("https://europe-west1-notes-synced.cloudfunctions.net/api/update");
    }

    private void performPostCall(String url, Map map) {

        RequestQueue queue = new RequestQueue(
            new DiskBasedCache(getCacheDir(), 1024 * 1024),
            new BasicNetwork(new HurlStack())
        );
        queue.start();


        try {
            StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray array = new Gson().fromJson(response, JSONArray.class);
                            Log.d("array", array.toString());

                            for(int i=0; i < array.length(); i++) {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject object = array.getJSONObject(i);

                                noteList.add(new Note(
                                    object.getString("title"),
                                    object.getString("body")g
                                ));
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        //Map<String, String> body = new HashMap<>();
                        //body.put("email", "fabian3@fabian.fabian");
                        //body.put("password", "fabianfabian");
                        return map;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("Authorization", "Bearer " + token);

                        return params;
                    }
            };
            queue.add(request);

        } catch(Exception e) {
            Log.d(TAG, e.toString());
        }
    }


    private void update(String url) {

        RequestQueue queue = new RequestQueue(
                new DiskBasedCache(getCacheDir(), 1024 * 1024),
                new BasicNetwork(new HurlStack())
        );
        queue.start();

        try {
            StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> body = new HashMap<>();
                        JsonArray content = new JsonArray();

                        for(Note note : noteList) {
                            JsonObject object = new JsonObject();
                            object.addProperty("title", note.getTitle());
                            object.addProperty("body", note.getBody());

                            //Map<String, String> object = new HashMap<>();
                            //object.put("body", note.getBody());
                            //object.put("title", note.getTitle());

                            content.add(object);
                        }

                        body.put("notes", content.toString());
                        return body;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("Authorization", "Bearer " + token);

                        return params;
                    }
            };
            queue.add(request);

        } catch(Exception e) {
            Log.d(TAG, e.toString());
        }
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
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
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

        firebaseAuth.getCurrentUser().getIdToken(true)
            .addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                @Override
                public void onSuccess(GetTokenResult getTokenResult) {
                    Log.d(TAG, getTokenResult.getToken());
                    token = getTokenResult.getToken();
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


















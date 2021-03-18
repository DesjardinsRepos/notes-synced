package com.example.notes_synced;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import javax.annotation.Nullable;

public class loginRegister extends AppCompatActivity {

    private final int AuthUIRequestCode = 10001;
    private static final String TAG = "loginRegister";

    @Override
    protected void onCreate(Bundle savedInstanceState) { // initialize Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) { // redirect to MainActivity if already signed in
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // when signin completed
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AuthUIRequestCode) {
            if(resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) { // new User
                    // show a tutorial or something
                }
                startActivity(new Intent(this, MainActivity.class));
                this.finish();

            } else {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(response == null) { // back button pressed
                    Log.d(TAG, "onActivityResult: user has canceled signin");
                } else { // error while signing in
                    Log.d(TAG, "onActivityResult:" + response.getError());
                }
            }
        }
    }

    public void handleLoginRegister(View view) { // start signin activity

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build()
                ))
                .setTosAndPrivacyPolicyUrls("https://tos.url", "https://privacyPolicy.url")
                .setTheme(R.style.LoginTheme)
                .setLogo(R.drawable.app_icon)
                //.setAlwaysShowSignInMethodScreen(true)
                .build(),
            AuthUIRequestCode
        );
    }
}
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
import java.util.List;

import javax.annotation.Nullable;

public class loginRegister extends AppCompatActivity {

    int AuthUIRequestCode = 10001;
    private static final String TAG = "loginRegister";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AuthUIRequestCode) {
            if(resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onActivityResult" + user.getEmail());
                if(user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) { // new User

                } else {
                    // old user
                }

                startActivity(new Intent(this, MainActivity.class));
                this.finish();

            } else {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(response == null) {
                    Log.d(TAG, "onActivityResult: user has canceled signin");
                } else {
                    Log.d(TAG, "onActivityResult:" + response.getError());
                }

            }
        }
    }

    public void handleLoginRegister(View view) {

        Intent loginRegister = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build()
                ))
                .setLogo(R.drawable.common_google_signin_btn_icon_dark)
                .build();

        startActivityForResult(loginRegister, AuthUIRequestCode);


    }
}
package com.example.sirirak.iotproj;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class AccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    AppCompatTextView tvDisplay;
    String providerId = "null";
    String uid = "null";
    String name = "null";
    String email = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mAuth = FirebaseAuth.getInstance();

        tvDisplay = findViewById(R.id.tv_display);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    for (UserInfo profile : user.getProviderData()) {
                        // Id of the provider (ex: google.com)
                        providerId = profile.getProviderId();
                        uid = profile.getUid();
                        name = profile.getDisplayName();
                        email = profile.getEmail();
                        //Uri photoUrl = profile.getPhotoUrl();
                    }
                setDisplay();
                } else {
                    // User is signed out
                    startActivity(new Intent(AccountActivity.this,MainActivity.class));
                }
                // ...
            }
        };
    }

    @SuppressLint("SetTextI18n")
    private void setDisplay() {
        tvDisplay.setText("provider : "+providerId+"\n"
                +"user id : "+uid+"\n"
                +"name : "+name+"\n"
                +"email : "+email);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }
}

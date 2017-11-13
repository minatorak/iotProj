package com.example.sirirak.iotproj.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.widget.Button;

import com.example.sirirak.iotproj.MainActivity;
import com.example.sirirak.iotproj.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    AppCompatTextView tvDisplay;
    AppCompatButton btnLogout;
    Button btnTest;
    String providerId = "null";
    String uid = "null";
    String name = "null";
    String email = "null";
    //Real time Database
    DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        findView();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                Log.d("AuthStateListener","");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Log.d("AuthStateListener","user != null");
                        name = user.getDisplayName();
                        email = user.getEmail();
                        //Uri photoUrl = user.getPhotoUrl();
                        uid = user.getUid();
                    setDisplay();
                    }
                    /*for (UserInfo profile : user.getProviderData()) {
                        // Id of the provider (ex: google.com)
                        providerId = profile.getProviderId();
                        uid = profile.getUid();
                        name = profile.getDisplayName();
                        email = profile.getEmail();
                        //Uri photoUrl = profile.getPhotoUrl();
                    }*/
                 else {
                    // User is signed out
                    startActivity(new Intent(AccountActivity.this,MainActivity.class));
                    finish();
                }
                // ...
            }
        };
        /*btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });*/
        btnLogout.setOnClickListener(view -> FirebaseAuth.getInstance().signOut());
        btnTest.setOnClickListener((view -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName("Jirawatee").build();
            if (user != null) {
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("update name", "User profile updated.");
                                }
                            }
                        });
            }
            DatabaseReference mUsersRef = mRootRef.child("users");
            mUsersRef.child(uid).setValue(name);
        }));
        mRootRef = FirebaseDatabase.getInstance().getReference();
    }

    private void findView() {
        tvDisplay = findViewById(R.id.tv_display);
        btnLogout = findViewById(R.id.btn_logout);
        btnTest = findViewById(R.id.btn_test);
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

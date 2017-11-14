package com.example.sirirak.iotproj.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sirirak.iotproj.AwesomeDialogFragment;
import com.example.sirirak.iotproj.R;
import com.example.sirirak.iotproj.model.ProcessModel;
import com.example.sirirak.iotproj.model.dataclass;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class AccountActivity extends AppCompatActivity implements AwesomeDialogFragment.OnDialogListener,OnLocationUpdatedListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient googleApiClient;
    private ValueEventListener valueEventListener;
    private AppCompatTextView tvDisplay;
    private DatabaseReference mUsersRef;
    private AppCompatButton btnLogout;
    private Button btnTest;
    private Button btnTest2;
    private TextView tvDetail;
    private ChildEventListener mChildEventListener;
    dataclass pro;
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


        mAuthListener = firebaseAuth -> {
            Log.d("AuthStateListener","");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Log.d("AuthStateListener","user != null");
                name = user.getDisplayName();
                email = user.getEmail();
                uid = user.getUid();
                setDisplay();
            }
            else {
                // User is signed out
                startActivity(new Intent(AccountActivity.this,MainActivity.class));
                finish();
            }
        };


        findView();
        setEventButton();
    }



    private void setEventButton() {

        btnLogout.setOnClickListener(view -> FirebaseAuth.getInstance().signOut());

        btnTest.setOnClickListener((view -> {
            DatabaseReference mUsersRef = mRootRef.child("users").child(uid);
            mUsersRef.child("pClient");
            mUsersRef.child("email").setValue(email);
        }));
        btnTest2.setOnClickListener(view -> {
            DatabaseReference mUsersRef = mRootRef.child("users").child(uid);
            mUsersRef.child("pClient").child("Latitude").setValue(10);
            mUsersRef.child("pClient").child("Longitude").setValue(10);
        });
    }

    private void findView() {
        tvDisplay = findViewById(R.id.tv_display);
        btnLogout = findViewById(R.id.btn_logout);
        btnTest = findViewById(R.id.btn_test);
        btnTest2 = findViewById(R.id.btn_test2);
        tvDetail = findViewById(R.id.tv_detail);
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
        mUsersRef = mRootRef.child("users").child(uid);
        mUsersRef.child("email").setValue(email);
        Log.d("onstart","before Listener");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("onDataChange","onDataChange");
                pro = dataSnapshot.getValue(dataclass.class);

                if (pro != null&&pro.process.getState() == 1) {
                    Log.d("onDataChange", "state = 1");
                    AwesomeDialogFragment fragment = new AwesomeDialogFragment.Builder()
                            .setMessage("Validate code is " + pro.process.getCodeValidate())
                            .setPosition("OK")
                            .setNegative("Cancel")
                            .build();
                    fragment.show(getSupportFragmentManager(), "dialog");
                }
                // ดึงค่าที่เป็น string จาก dataSanpshot
                // ดึงค่าจาก dataSanpshot แบบ data model
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("", databaseError.getMessage());
            }
        };
        mUsersRef.addValueEventListener(valueEventListener);
        SmartLocation.with(this)
                .location()
                .start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        if (valueEventListener != null) {
            mUsersRef.removeEventListener(valueEventListener);
        }
        SmartLocation.with(this)
                .location()
                .stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();

    }

    @Override
    public void onPositiveButtonClick() {

    }

    @Override
    public void onNegativeButtonClick() {

    }

    @Override
    public void onLocationUpdated(Location location) {

    }
}

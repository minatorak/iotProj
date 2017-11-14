package com.example.sirirak.iotproj.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sirirak.iotproj.AwesomeDialogFragment;
import com.example.sirirak.iotproj.Manifest;
import com.example.sirirak.iotproj.R;
import com.example.sirirak.iotproj.model.dataclass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnGeofencingTransitionListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geofencing.utils.TransitionGeofence;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesWithFallbackProvider;

public class AccountActivity extends AppCompatActivity implements AwesomeDialogFragment.OnDialogListener,OnLocationUpdatedListener, OnActivityUpdatedListener, OnGeofencingTransitionListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ValueEventListener valueEventListener;
    private AppCompatTextView tvDisplay;
    private DatabaseReference mUsersRef;
    private AppCompatButton btnLogout;
    private Button btnTest;
    private Button btnTest2;
    private TextView tvDetail;
    private dataclass pro;
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
        Log.d("AccountActivity","set Instance ");

        mAuthListener = firebaseAuth -> {
            Log.d("AccountActivity","AuthStateListener");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Log.d("AccountActivity","user != null");
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
            if (ContextCompat.checkSelfPermission(AccountActivity.this, Manifest.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AccountActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
                return;
            }else{
                startLocation();
            }

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


        /*if(SmartLocation.with(this).location().state().locationServicesEnabled()) {
            SmartLocation.with(this).location().config(LocationParams.NAVIGATION).start(this);
        } else {
            Toast.makeText(this, "Location Unavailabled",
                    Toast.LENGTH_SHORT).show();
        }*/
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationUpdated(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float accuracy = location.getAccuracy();
        float bearing = location.getBearing();
        String provider = location.getProvider();
        tvDetail.setText("latitude :"+latitude+"\nlongitude :"+longitude
                        +"\naccuracy :"+accuracy+"\nbearing :"+bearing
                        +"\nprovider :"+provider
        );
    }

    @Override
    public void onActivityUpdated(com.google.android.gms.location.DetectedActivity detectedActivity) {

    }

    @Override
    public void onGeofenceTransition(TransitionGeofence transitionGeofence) {

    }
}

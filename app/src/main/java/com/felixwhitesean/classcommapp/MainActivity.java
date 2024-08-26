package com.felixwhitesean.classcommapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "UserLoginPrefs";
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    private static final String CURRENT_USER_ID = "CurrentUserId";
    private AlarmManager alarmManager;
    String userUID, userDetails;
    DocumentReference docRef;
    FirebaseFirestore db;
//    SharedPreferences sharedPref;

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.landing_page);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Check if user is already logged in
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean(IS_LOGGED_IN, false);
        userUID = prefs.getString(CURRENT_USER_ID, null);
        if(userUID == null){
            Intent intent = new Intent( MainActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }
        
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("users").document(userUID);
        
        Data inputData = new Data.Builder().putString("userId", userUID).build();
        OneTimeWorkRequest fetchUserDetailsWork = new OneTimeWorkRequest.Builder(UserDetailsWorker.class).setInputData(inputData).build();
        WorkManager.getInstance(getApplicationContext()).enqueue(fetchUserDetailsWork);

        Button getStartedButton = findViewById(R.id.rectangle_28);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to authentication_page_activity
                if (!isLoggedIn) {
                    Toast.makeText(MainActivity.this, "Sign up to continue", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
                else {
                    // User is logged in, redirect to ScheduleSettings
//                    initializeWorker();
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String course = document.getString("course");
                                    SharedPreferences sp = getSharedPreferences("UserDetails", MODE_PRIVATE);
                                    SharedPreferences.Editor edt = sp.edit();
                                    edt.putString("timetable", course); // Assuming latestTimetable is your new selection
                                    edt.apply();
                                    Toast.makeText(MainActivity.this, course, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity.this, "didn't find the user", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(MainActivity.this, "Process not successfull", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
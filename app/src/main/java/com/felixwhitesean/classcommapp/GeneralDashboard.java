package com.felixwhitesean.classcommapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GeneralDashboard extends AppCompatActivity {
    TextView title, coursename;
    FirebaseFirestore db;
    String userUID;
    Button menuBtn, todaysSchedule, upcoming_notification_btn;
    SharedPreferences sharedPref;
    private static final String PREFS_NAME = "UserLoginPrefs";
    private static final String CURRENT_USER_ID = "CurrentUserId";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_dashboard);
        todaysSchedule = findViewById(R.id.todays_schedule);
        upcoming_notification_btn = findViewById(R.id.upcoming_notification_btn);
        title = findViewById(R.id.title);
        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userUID = sharedPref.getString(CURRENT_USER_ID, null);
        db = FirebaseFirestore.getInstance();
        coursename = findViewById(R.id.computer_technology);

        DocumentReference docRef = db.collection("users").document(userUID);

        if(userUID != null) {
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Document data successfully retrieved
                            String course = document.getString("course");
                            String department = document.getString("department");
                            String courseDetails = course+" ("+department+")";
                            coursename.setText(courseDetails);
//                            Toast.makeText(GeneralDashboard.this, "Username: " + username, Toast.LENGTH_SHORT).show();
//                            Toast.makeText(GeneralDashboard.this, "Email: " + email, Toast.LENGTH_SHORT).show();
//                            Log.d("Firebase", "User Name: " + username);
//                            Log.d("Firebase", "User Email: " + email);
                        } else {
                            Toast.makeText(GeneralDashboard.this, "No such document", Toast.LENGTH_SHORT).show();
                            Log.d("Firebase", "No such document");
                        }
                    } else {
                        Toast.makeText(GeneralDashboard.this, "get failed with "+task.getException(), Toast.LENGTH_SHORT).show();
                        Log.d("Firebase", "get failed with ", task.getException());
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "No user details found", Toast.LENGTH_SHORT).show();
            Intent toHome = new Intent(GeneralDashboard.this, userInfoActivity.class);
            startActivity(toHome);
            finish();
        }
        menuBtn = findViewById(R.id.icon);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ActivityA
                Intent intent = new Intent(GeneralDashboard.this, Menu.class);
                startActivity(intent);
            }
        });
        todaysSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Today's Schedule");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new TodaysSchedule())
                        .commit();
            }
        });
        upcoming_notification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("Upcoming Notification");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new UpcomingNotifications())
                        .commit();
            }
        });
        if(savedInstanceState == null) {
            title.setText("Today's Schedule");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new TodaysSchedule())
                    .commit();
        }
    }
}
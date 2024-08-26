package com.felixwhitesean.classcommapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneralDashboard extends AppCompatActivity {
    TextView title, coursename;
    FirebaseFirestore db;
    String userUID, course, courseDetails, department, userDetails;
    DocumentReference docRef, courseSchedule,docRef2, ref3;
    Button menuBtn, todaysSchedule, upcoming_notification_btn;
    SharedPreferences sharedPref, sessionPref;
    private static final String PREFS_NAME = "UserLoginPrefs";
    private static final String CURRENT_USER_ID = "CurrentUserId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_dashboard);
        todaysSchedule = findViewById(R.id.todays_schedule);
        upcoming_notification_btn = findViewById(R.id.upcoming_notification_btn);
        title = findViewById(R.id.title);
        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userUID = sharedPref.getString(CURRENT_USER_ID, null);
        db = FirebaseFirestore.getInstance();
        coursename = findViewById(R.id.computer_technology);
        sessionPref = getSharedPreferences("sessionVariables", MODE_PRIVATE);
        userDetails = getSharedPreferences("UserDetails", MODE_PRIVATE).getString("user-details", null);
//        Toast.makeText(this, userDetails, Toast.LENGTH_SHORT).show();

        docRef = db.collection("users").document(userUID);
//        docRef2 = db.collection("timetable").document("ct-timetable");
        JSONObject currentUserDetails = getUserDetails(userDetails);

        if(currentUserDetails != null){
            course = null;
            department = null;
            try {
                course = currentUserDetails.getString("course");
                department = currentUserDetails.getString("department");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            courseDetails = course + " (" + department + ")";
            coursename.setText(courseDetails);
            setCurrentUserCourseName(course);
        }
        String uuidString = sessionPref.getString("workRequestId", null);
        if (uuidString != null) {
            UUID workRequestId = UUID.fromString(uuidString);

            WorkManager.getInstance(getApplicationContext())
                    .getWorkInfoByIdLiveData(workRequestId)
                    .observe(this, workInfo -> {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            // Get the final data from the second worker

                            String todaySessions = getSharedPreferences("UserDetails", MODE_PRIVATE).getString("TodaySessions", null);
//                            Toast.makeText(GeneralDashboard.this, todaySessions, Toast.LENGTH_LONG).show();
                            assert todaySessions != null;
                            todaySessions = todaySessions.substring(1, todaySessions.length() - 1);
                            String[] sessionsArray = todaySessions.split(",");

                            AtomicInteger counter = new AtomicInteger(0);  // Counter for Firestore tasks
                            JSONArray schedulesArray = new JSONArray();    // Array to store session details

                            for (int i = 0; i < sessionsArray.length; i++) {
                                sessionsArray[i] = sessionsArray[i].trim();  // Trim spaces from session names

                                db.collection("timetable")
                                        .document(course)  // Main document ID
                                        .collection("session details")
                                        .document(sessionsArray[i]).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        try {
                                                            // Create a new JSONObject for each session
                                                            JSONObject sessionObject = new JSONObject();
                                                            sessionObject.put("Room", document.getString("room"));
                                                            sessionObject.put("Duration", document.getString("time_period"));
                                                            sessionObject.put("Lecturer", document.getString("lecturerName"));
                                                            sessionObject.put("Unit", document.getString("unit"));

                                                            // Add the sessionObject to the schedulesArray
                                                            schedulesArray.put(sessionObject);

                                                            // Debugging: Show intermediate result
                                                            Toast.makeText(getApplicationContext(), "Session fetched: " + sessionObject.toString(), Toast.LENGTH_SHORT).show();

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        Toast.makeText(GeneralDashboard.this, "Could not find session document", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(GeneralDashboard.this, "Could not fetch session details", Toast.LENGTH_SHORT).show();
                                                }

                                                // Increment the counter after each task completes
                                                counter.incrementAndGet();

                                                // Check if all tasks are done
                                                if (counter.get() == sessionsArray.length) {
                                                    // All Firestore tasks have been completed, now display final result
                                                    Toast.makeText(getApplicationContext(), "Final Schedule Array: " + schedulesArray.toString(), Toast.LENGTH_LONG).show();

                                                    // Loop through schedulesArray to display each session's details
                                                    JSONArray todayScheduleArray = new JSONArray();
                                                    for (int n = 0; n < schedulesArray.length(); n++) {
                                                        try {
                                                            JSONObject session = schedulesArray.getJSONObject(n);
                                                            String room = session.getString("Room");
                                                            String duration = session.getString("Duration");
                                                            String lecturer = session.getString("Lecturer");
                                                            String unit = session.getString("Unit");

                                                            JSONObject todaysScheduleDetails = new JSONObject();
                                                            todaysScheduleDetails.put("Unit", unit);
                                                            todaysScheduleDetails.put("Lecturer", lecturer);

                                                            todayScheduleArray.put(todaysScheduleDetails);

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    String jsonString = todayScheduleArray.toString();
                                                    todaysSchedule.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            getTodaySchedule(jsonString);
                                                        }

                                                    });
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(GeneralDashboard.this, "Failed to fetch document", Toast.LENGTH_SHORT).show();
                                                // Increment counter even if request fails
                                                counter.incrementAndGet();

                                                // Ensure completion handling when all tasks are done
                                                if (counter.get() == sessionsArray.length) {
                                                    // Final result even if some tasks failed
                                                    Toast.makeText(getApplicationContext(), "Final Schedule Array: " + schedulesArray.toString(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                        }
                            //                                Toast.makeText(GeneralDashboard.this, todaySessions, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(GeneralDashboard.this, "Today sessions not available", Toast.LENGTH_SHORT).show();
                        }
                        // Display each session's details
//                        courseDetails = course + " (" + department + ")";
//                        coursename.setText(courseDetails);
//                        setCurrentUserCourseName(course);
                            // Use the data in your activity
//                            Toast.makeText(this, finalData, Toast.LENGTH_LONG).show();
//                        }
                    });
        }
        else{
            Toast.makeText(this, "No user information. Please restart the app and try again", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(GeneralDashboard.this, MainActivity.class);
            startActivity(intent);
        }
//        if (savedInstanceState == null) {
//            todaysSchedule.callOnClick();
//        }
        menuBtn = findViewById(R.id.icon);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Menu
                Intent intent = new Intent(GeneralDashboard.this, Menu.class);
                startActivity(intent);
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


    }

    public void setCurrentUserCourseName(String course) {
        this.course = course;
    }

    public void getTodaySchedule(String schedulesString) {
        Fragment todaysScheduleFragment = new TodaysSchedule();  // Create instance of the Fragment
        // Create a bundle to pass data
        Bundle bundle = new Bundle();
        bundle.putString("schedules", schedulesString);
        // Set the bundle as arguments to the fragment
        todaysScheduleFragment.setArguments(bundle);
        title.setText("Today's Schedule");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, todaysScheduleFragment)
                .commit();

    }
    public JSONObject getUserDetails(String userDetails) {
        JSONObject result = null;
        if (userDetails != null) {
            try {
                result = new JSONObject(userDetails);// Convert String to JSONObject
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}

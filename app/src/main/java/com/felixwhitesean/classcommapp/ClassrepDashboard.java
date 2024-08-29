package com.felixwhitesean.classcommapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.work.WorkManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.StringValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassrepDashboard extends AppCompatActivity {
    TextView title, lecturerName, courseDisplay;
    View notificationIndicator, todayIndicator;
    FirebaseFirestore db;
    String userUID, course, courseDetails, department, userDetails, username;
    DocumentReference docRef;
    Button menuBtn, todaysSchedule, upcoming_notification_btn;
    SharedPreferences sharedPref, sessionPref;
    private static final String PREFS_NAME = "UserLoginPrefs";
    private static final String CURRENT_USER_ID = "CurrentUserId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_dashboard);
        title = findViewById(R.id.lecturer_s_dashboard);
        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userUID = sharedPref.getString(CURRENT_USER_ID, null);
        db = FirebaseFirestore.getInstance();
        lecturerName = findViewById(R.id.john_okwaya);
        courseDisplay = findViewById(R.id.scit);
        CalendarView calendarView = findViewById(R.id.calendarView);

        sessionPref = getSharedPreferences("sessionVariables", MODE_PRIVATE);
        userDetails = getSharedPreferences("UserDetails", MODE_PRIVATE).getString("user-details", null);
//        Toast.makeText(this, userDetails, Toast.LENGTH_SHORT).show();

        docRef = db.collection("users").document(userUID);
        JSONObject currentUserDetails = getUserDetails(userDetails);

        if(currentUserDetails != null){
            course = null;
            department = null;
            try {

                course = currentUserDetails.getString("course");
                department = currentUserDetails.getString("department");
                username = currentUserDetails.getString("username");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
//            lecturerName.setText(username);
//            courseDisplay.setText(course);

            Toast.makeText(this, username + " " + course, Toast.LENGTH_SHORT).show();
        }
        String uuidString = sessionPref.getString("workRequestId", null);
        if (uuidString != null) {
            UUID workRequestId = UUID.fromString(uuidString);

            WorkManager.getInstance(getApplicationContext())
                    .getWorkInfoByIdLiveData(workRequestId)
                    .observe(this, workInfo -> {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            // Get the final data from the second worker
                            String todaySchedules = getSharedPreferences("UserDetails", MODE_PRIVATE).getString("TodayClasses", null);
                            String sessions = getSharedPreferences("UserDetails", MODE_PRIVATE).getString("TodaySchedule", null);

                            List<String> scheduleList = new ArrayList<>();
                            if (todaySchedules != null) {
                                try {
                                    JSONArray jsonArray = new JSONArray(todaySchedules);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        scheduleList.add(jsonArray.getString(i));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

//                            Toast.makeText(ClassrepDashboard.this, todaySchedules, Toast.LENGTH_LONG).show();
                            assert todaySchedules != null;

                            AtomicInteger counter = new AtomicInteger(0);  // Counter for Firestore tasks
                            JSONArray schedulesArray = new JSONArray();    // Array to store session details

                            for (int i = 0; i < scheduleList.size(); i++) {
                                String schedule = scheduleList.get(i);
                                db.collection("timetable")
                                        .document(course)  // Main document ID
                                        .collection("session details")
                                        .document(schedule).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        try {
                                                            // Create a new JSONObject for each session
                                                            JSONObject sessionObject = new JSONObject();
                                                            sessionObject.put("Lecturer", document.getString("lecturerName"));
                                                            sessionObject.put("Unit", document.getString("unit"));
                                                            sessionObject.put("Period", document.getString("time_period"));
                                                            schedulesArray.put(sessionObject);

                                                            // Debugging: Show intermediate result
//                                                            Toast.makeText(getApplicationContext(), "Session fetched: " + sessionObject.toString(), Toast.LENGTH_SHORT).show();

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        Toast.makeText(ClassrepDashboard.this, "Could not find session document", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(ClassrepDashboard.this, "Could not fetch session details", Toast.LENGTH_SHORT).show();
                                                }
                                                counter.incrementAndGet();

                                                // Check if all tasks are done
                                                if (counter.get() == scheduleList.size()) {
                                                    // All Firestore tasks have been completed, now display final result
//                                                    Toast.makeText(getApplicationContext(), "Final Schedule Array: " + schedulesArray.toString(), Toast.LENGTH_LONG).show();

                                                    // Loop through schedulesArray to display each session's details
                                                    for (int n = 0; n < schedulesArray.length(); n++) {
                                                        if (sessions != null) {
                                                            try {
                                                                JSONArray sesionsArray = new JSONArray(sessions);
                                                                JSONObject session = schedulesArray.getJSONObject(n);
                                                                boolean classSession = sesionsArray.getBoolean(n);

                                                                String lecturer = session.getString("Lecturer");
                                                                String unit = session.getString("Unit");
                                                                String time_period = session.getString("Period");
//                                                                Toast.makeText(ClassrepDashboard.this, lecturer + unit + time_period, Toast.LENGTH_SHORT).show();

                                                                //Control the display based on the above data
                                                                LinearLayout containerLayout = findViewById(R.id.container);
                                                                containerLayout.setOrientation(LinearLayout.VERTICAL);

                                                                LinearLayout linearLayout = getLinearLayout(time_period,unit, lecturer,classSession, n);
                                                                containerLayout.addView(linearLayout);
                                                                LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
                                                                        LinearLayout.LayoutParams.MATCH_PARENT,  // equivalent to android:layout_width="match_parent"
                                                                        LinearLayout.LayoutParams.WRAP_CONTENT   // equivalent to android:layout_height="wrap_content"
                                                                );
                                                                int marginInDp = (int) TypedValue.applyDimension(
                                                                        TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
                                                                layoutParam.setMargins(marginInDp, marginInDp, marginInDp, marginInDp);
//                                                                int paddingInDp = (int) TypedValue.applyDimension(
//                                                                        TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
//                                                                layoutParam.setMargins(paddingInDp, paddingInDp, paddingInDp, paddingInDp);
                                                                if(classSession){
                                                                    linearLayout.setBackgroundResource(R.drawable.present);
                                                                }
                                                                else{
                                                                    linearLayout.setBackgroundResource(R.drawable.absent);
                                                                }
                                                                // Set padding (equivalent to android:padding="10dp")
                                                                int paddingInDp = (int) TypedValue.applyDimension(
                                                                        TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                                                                linearLayout.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp);
                                                                linearLayout.setLayoutParams(layoutParam);
                                                            } catch (JSONException e) {
                                                                throw new RuntimeException(e);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ClassrepDashboard.this, "Failed to fetch document", Toast.LENGTH_SHORT).show();
                                                // Increment counter even if request fails
                                                counter.incrementAndGet();

                                                // Ensure completion handling when all tasks are done
                                                if (counter.get() == scheduleList.size()) {
                                                    // Final result even if some tasks failed
                                                    Toast.makeText(getApplicationContext(), "Final Schedule Array: " + schedulesArray.toString(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                            }
                            }else{
                            Toast.makeText(ClassrepDashboard.this, "Today sessions not available", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(this, "No user information. Please restart the app and try again", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ClassrepDashboard.this, MainActivity.class);
            startActivity(intent);
        }
        menuBtn = findViewById(R.id.icon);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Menu
                Intent intent = new Intent(ClassrepDashboard.this, Menu.class);
                startActivity(intent);
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Do something with the selected date
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                Toast.makeText(ClassrepDashboard.this, "Selected date: " + selectedDate, Toast.LENGTH_SHORT).show();
            }
        });
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
    public LinearLayout getLinearLayout(String time_period,String unit, String lecturer,boolean classSession, int i){
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());

        //time_period Textview
        TextView time_period_tv  = new TextView(getApplicationContext());
        time_period_tv.setText(time_period);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        time_period_tv.setLayoutParams(layoutParam);
        time_period_tv.setTextColor(Color.parseColor("#FFFFFF"));  // Red color


        LinearLayout middleLinear = new LinearLayout(getApplicationContext());
        middleLinear.setOrientation(LinearLayout.VERTICAL);
        int marginInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(marginInDp, 0, marginInDp, marginInDp);
        middleLinear.setLayoutParams(param);

        TextView unitName = new TextView(this);
        unitName.setText(unit);
        unitName.setTextColor(Color.parseColor("#FFFFFF"));

        TextView lecturerName = new TextView(this);
        lecturerName.setText(lecturer);
        lecturerName.setTextColor(Color.parseColor("#FFFFFF"));

        middleLinear.addView(unitName);
        middleLinear.addView(lecturerName);

        RelativeLayout switchBtnLyt = new RelativeLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        switchBtnLyt.setLayoutParams(params);
        int margInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -5, getResources().getDisplayMetrics());

        SwitchCompat switchCompat = getSwitchCompat(classSession, i);


        switchBtnLyt.addView(switchCompat);

        //Return the created view to parent LinearLayout
        linearLayout.addView(time_period_tv);
        linearLayout.addView(middleLinear);
        linearLayout.addView(switchBtnLyt);
        return linearLayout;
    }

    private @NonNull SwitchCompat getSwitchCompat(boolean classSession, int i) {
        SwitchCompat switchCompat = new SwitchCompat(this);
        switchCompat.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams switchparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, // Width is wrap_content
                RelativeLayout.LayoutParams.WRAP_CONTENT                  // Height in dp (34dp converted to pixels)
        );
        switchparams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        switchparams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        switchCompat.setLayoutParams(switchparams);

        // Set the switch to ON if classSession is true
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Set colors for when the switch is ON
                    switchCompat.setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.toggleOn))); // Example color for ON state
                    switchCompat.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.rectangle_1_color))); // Example color for OFF state
                    updateFirebase(true, i);
                    LinearLayout containerLayout = findViewById(R.id.container);
                    containerLayout.requestLayout();
                } else {
                    // Set colors for when the switch is OFF
                    switchCompat.setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.rectangle_1_color))); // Example color for OFF state
                    switchCompat.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.toggleBg))); // Example color for OFF state
                    updateFirebase(false, i);
                    switchCompat.requestLayout();
                    LinearLayout containerLayout = findViewById(R.id.container);
                    containerLayout.requestLayout();
                }
            }
        });
        switchCompat.setChecked(classSession);
        return switchCompat;
    }

    private void updateFirebase(boolean isChecked, int i) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> updateData = new HashMap<>();
        String ToUpdate = "session"+i;
//        Toast.makeText(this, ToUpdate, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
        updateData.put( ToUpdate, isChecked); // Field you want to update in Firebase

        db.collection("timetable") // Specify your collection
                .document(course) // Specify your document ID
                .update(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ClassrepDashboard.this, "Database updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ClassrepDashboard.this, "Error updating document", Toast.LENGTH_SHORT).show();
//                        Log.w("FirebaseUpdate", "Error updating document", e);
                    }
                });
    }
}

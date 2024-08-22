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
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "UserLoginPrefs";
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    private AlarmManager alarmManager;


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

        Button getStartedButton = findViewById(R.id.rectangle_28);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
                Log.d("acknowledgment", "Getting started button clicked");
                // Navigate to authentication_page_activity
                if (isLoggedIn) {
                    // User is logged in, redirect to ScheduleSettings
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Sign up to continue", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    private void setAlarm() {
        // Set the alarm to start 30 seconds from now
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 30); // Set the alarm to trigger after 30 seconds

        // Create an intent to the BroadcastReceiver
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Get the AlarmManager service
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Set the exact alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        Log.d("Alarm", "Alarm set for 30 seconds from now.");
    }
}
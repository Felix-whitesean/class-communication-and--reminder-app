package com.felixwhitesean.classcommapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GeneralDashboard extends AppCompatActivity {
    TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_dashboard);

        Button todaysSchedule = findViewById(R.id.todays_schedule);
        Button upcoming_notification_btn = findViewById(R.id.upcoming_notification_btn);
        title = findViewById(R.id.title);

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
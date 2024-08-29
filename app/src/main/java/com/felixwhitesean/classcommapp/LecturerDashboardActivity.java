package com.felixwhitesean.classcommapp;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LecturerDashboardActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_dashboard);
        CalendarView calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Do something with the selected date
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                Toast.makeText(LecturerDashboardActivity.this, selectedDate, Toast.LENGTH_SHORT).show();
            }
        });

    }
}

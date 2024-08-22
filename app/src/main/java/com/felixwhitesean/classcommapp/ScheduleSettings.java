package com.felixwhitesean.classcommapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ScheduleSettings extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_settings);
        Button menuBtn;

        menuBtn = findViewById(R.id.icon);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ActivityA
                Intent intent = new Intent(ScheduleSettings.this, Menu.class);
                startActivity(intent);
            }
        });
    }
}

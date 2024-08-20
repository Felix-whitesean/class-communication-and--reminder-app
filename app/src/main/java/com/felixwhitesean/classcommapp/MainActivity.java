package com.felixwhitesean.classcommapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "UserLoginPrefs";
    private static final String IS_LOGGED_IN = "IsLoggedIn";

//    FileInputStream serviceAccount =
//            new FileInputStream("path/to/serviceAccountKey.json");
//
//    FirebaseOptions options = new FirebaseOptions.Builder()
//            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//            .setDatabaseUrl("https://developer1-8622a-default-rtdb.firebaseio.com")
//            .build();
//
//FirebaseApp.initializeApp(options);

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
                Log.d("acknowledgment","Getting started button clicked");
//                // Navigate to authentication_page_activity
                if (isLoggedIn) {
                    // User is logged in, redirect to ScheduleSettings
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Sign up to continue", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
